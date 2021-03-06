package org.bladerunnerjs.appserver.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;


public class TokenReplacingReaderTest
{
	@Rule
	public final ExpectedException exception = ExpectedException.none();
	
	private TokenFinder mockTokenFinder;
	private TokenFinder brjsTokenFinder;
    private MissingTokenHandler mockTokenReplacementHandler = mock(MissingTokenHandler.class);
    
    private static final String APP_NAME = "app";
	
	@SuppressWarnings("unchecked")
	@Before
	public void setup() throws Exception
	{
		brjsTokenFinder = mock(TokenFinder.class);
		when(brjsTokenFinder.findTokenValue("BRJS.TOKEN")).thenReturn("brjs token replacement");
		when(brjsTokenFinder.findTokenValue("BRJS.EMPTY.TOKEN")).thenReturn("");
		when(brjsTokenFinder.findTokenValue("BRJS.NULL.TOKEN")).thenReturn(null);
		when(brjsTokenFinder.findTokenValue("BRJS.EXCEPTION.THROWING.TOKEN")).thenThrow(TokenReplacementException.class);
		
		mockTokenFinder = mock(TokenFinder.class);
		when(mockTokenFinder.findTokenValue("A.TOKEN")).thenReturn("token replacement");
		when(mockTokenFinder.findTokenValue("AN.EMPTY.TOKEN")).thenReturn("");
		when(mockTokenFinder.findTokenValue("A.NULL.TOKEN")).thenReturn(null);
		when(mockTokenFinder.findTokenValue("EXCEPTION.THROWING.TOKEN")).thenThrow(TokenReplacementException.class);
		when(mockTokenFinder.findTokenValue("LONG.TOKEN.REPLACEMENT")).thenReturn(StringUtils.leftPad("", 5000, "0") );
	}

	@After
	public void teardown() throws Exception
	{
		
	}
	
	@Test
	public void testfindTokenValueIsPerformedForToken() throws Exception
	{
		Reader tokenisingReader = new TokenReplacingReader( APP_NAME, mockTokenFinder, new StringReader("@A.TOKEN@") );
		String replacedContent = IOUtils.toString( tokenisingReader );
		assertEquals("token replacement", replacedContent);
		verify(mockTokenFinder, times(1)).findTokenValue("A.TOKEN");
	}
	
	@Test
	public void testTokensCanBeReplacedBackToBack() throws Exception
	{
		Reader tokenisingReader = new TokenReplacingReader( APP_NAME, mockTokenFinder, new StringReader("@A.TOKEN@@A.TOKEN@@A.TOKEN@") );
		String replacedContent = IOUtils.toString( tokenisingReader );
		assertEquals("token replacementtoken replacementtoken replacement", replacedContent);
	}
	
	@Test
	public void testTokensMustBeUppcaseAndDots() throws Exception
	{
		Reader tokenisingReader = new TokenReplacingReader( APP_NAME, mockTokenFinder, new StringReader("@A.token@") );
		String replacedContent = IOUtils.toString( tokenisingReader );
		assertEquals("@A.token@", replacedContent);
		verify(mockTokenFinder, times(0)).findTokenValue(any(String.class));
	}
	
	@Test
	public void testTokensMustBeContainedWithin2AtSymbols() throws Exception
	{
		Reader tokenisingReader = new TokenReplacingReader( APP_NAME, mockTokenFinder, new StringReader("@A.token") );
		String replacedContent = IOUtils.toString( tokenisingReader );
		assertEquals("@A.token", replacedContent);
		verify(mockTokenFinder, times(0)).findTokenValue(any(String.class));
	}
	
	@Test
	public void testTokensCannotContainInvalidChars() throws Exception
	{
		Reader tokenisingReader = new TokenReplacingReader( APP_NAME, mockTokenFinder, new StringReader("@A_TOKEN@") );
		String replacedContent = IOUtils.toString( tokenisingReader );
		assertEquals("@A_TOKEN@", replacedContent);
		verify(mockTokenFinder, times(0)).findTokenValue(any(String.class));
	}
	
	@Test
	public void testTwoAtSymbolsArentAValidToken() throws Exception
	{
		Reader tokenisingReader = new TokenReplacingReader( APP_NAME, mockTokenFinder, new StringReader("@@") );
		String replacedContent = IOUtils.toString( tokenisingReader );
		assertEquals("@@", replacedContent);
		verify(mockTokenFinder, times(0)).findTokenValue(any(String.class));
	}
	
	@Test
	public void tokenStringThatIsntClosedIsOutputAsTheTokenString() throws Exception
	{
		Reader tokenisingReader = new TokenReplacingReader( APP_NAME, mockTokenFinder, new StringReader("@A.TOKEN") );
		String replacedContent = IOUtils.toString( tokenisingReader );
		assertEquals("@A.TOKEN", replacedContent);
	}
	
	@Test
	public void tokenAfterASingleAtSymbolIsReplaced() throws Exception
	{
		Reader tokenisingReader = new TokenReplacingReader( APP_NAME, mockTokenFinder, new StringReader("@Foo@A.TOKEN@") );
		String replacedContent = IOUtils.toString( tokenisingReader );
		assertEquals("@Footoken replacement", replacedContent);
	}
	
	@Test
	public void testJndiIsfindTokenValuePerformedForTokenInsideOfALargerString() throws Exception
	{
		Reader tokenisingReader = new TokenReplacingReader( APP_NAME, mockTokenFinder, new StringReader("this is a @A.TOKEN@ :-)") );
		String replacedContent = IOUtils.toString( tokenisingReader );
		assertEquals("this is a token replacement :-)", replacedContent);
		verify(mockTokenFinder, times(1)).findTokenValue("A.TOKEN");
	}

	@Test
	public void tokenReplacementWorksForEmptyStringValues() throws Exception {
		Reader tokenisingReader = new TokenReplacingReader( APP_NAME, mockTokenFinder, new StringReader("@AN.EMPTY.TOKEN@") );
		String replacedContent = IOUtils.toString(tokenisingReader);
		assertEquals("", replacedContent);
	}

    @Test
    public void testEmptyStringIsUsedIfTokenReaderReturnsNull() throws Exception
    {
        Reader tokenisingReader = new TokenReplacingReader( APP_NAME, mockTokenFinder, new StringReader("@A.NULL.TOKEN@") );
        String replacedContent = IOUtils.toString(tokenisingReader);
        assertEquals("", replacedContent);
    }


    @Test
	public void testExceptionIsThrownIfTokenFinderThrowsAnInvalidTokenException() throws Exception
	{
		exception.expect(IllegalArgumentException.class);
		Reader tokenisingReader = new TokenReplacingReader( APP_NAME, mockTokenFinder, new StringReader("@EXCEPTION.THROWING.TOKEN@") );
		IOUtils.readLines(tokenisingReader);
	}
	
	@Test
	public void longTokenReplacementsCanBeUsed() throws Exception
	{
		Reader tokenisingReader = new TokenReplacingReader( APP_NAME, mockTokenFinder, new StringReader("@LONG.TOKEN.REPLACEMENT@") );
		String replacedContent = IOUtils.toString( tokenisingReader );
		assertEquals(StringUtils.leftPad("", 5000, "0"), replacedContent);
	}
	
	@Test
	public void tokenStringsCanSpanBufferLimits() throws Exception
	{
		Reader tokenisingReader = new TokenReplacingReader( APP_NAME, mockTokenFinder, new StringReader(
				StringUtils.leftPad("", 4094, "0")+" @A.TOKEN@ "+StringUtils.leftPad("", 4094, "0"))
		);
		String replacedContent = IOUtils.toString( tokenisingReader );
		assertEquals( 
				StringUtils.leftPad("", 4094, "0")+" token replacement "+StringUtils.leftPad("", 4094, "0")
		, replacedContent);
	}
	
	@Test
	public void invalidTokenStringsCanSpanBufferLimits() throws Exception
	{
		Reader tokenisingReader = new TokenReplacingReader( APP_NAME, mockTokenFinder, new StringReader(
				StringUtils.leftPad("", 4094, "0")+" @A INVALID TOKEN "+StringUtils.leftPad("", 4094, "0"))
		);
		String replacedContent = IOUtils.toString( tokenisingReader );
		assertEquals( 
				StringUtils.leftPad("", 4094, "0")+" @A INVALID TOKEN "+StringUtils.leftPad("", 4094, "0")
		, replacedContent);
	}
	
	@Test
	public void invalidTokenStringsCanSpanBufferLimitsWhenForcedToSpanByALargeTokenRepacement() throws Exception
	{
		when(mockTokenFinder.findTokenValue("LONG.TOKEN.REPLACEMENT")).thenReturn(StringUtils.leftPad("", 4094, "0") );
		Reader tokenisingReader = new TokenReplacingReader( APP_NAME, mockTokenFinder, new StringReader(
				"@LONG.TOKEN.REPLACEMENT@ @A INVALID TOKEN"
		));
		String replacedContent = IOUtils.toString( tokenisingReader );
		assertEquals( 
				StringUtils.leftPad("", 4094, "0")+" @A INVALID TOKEN"
		, replacedContent);
	}
	
	@Test
	public void tokenStringsCanSpanMultipleBufferLimits() throws Exception
	{
		Reader tokenisingReader = new TokenReplacingReader( APP_NAME, mockTokenFinder, new StringReader(
				StringUtils.leftPad("", 12286, "0")+" @A.TOKEN@ "+StringUtils.leftPad("", 12286, "0"))
		);
		String replacedContent = IOUtils.toString( tokenisingReader );
		assertEquals( 
				StringUtils.leftPad("", 12286, "0")+" token replacement "+StringUtils.leftPad("", 12286, "0")
		, replacedContent);
	}
	
	@Test
	public void tokensAreReplacedInsideOfLargeContent() throws Exception
	{
		for (int padLength : Arrays.asList(4096, 5000, 10000)) {
    		Reader tokenisingReader = new TokenReplacingReader( APP_NAME, mockTokenFinder, new StringReader(
    				StringUtils.leftPad("", padLength, "0")+" @A.TOKEN@ "+StringUtils.leftPad("", padLength, "0"))
    		);
    		String replacedContent = IOUtils.toString( tokenisingReader );
    		assertEquals( 
    				StringUtils.leftPad("", padLength, "0")+" token replacement "+StringUtils.leftPad("", padLength, "0")
    		, replacedContent);
		}
	}
	
	@Test
	public void closeMethodClosesTheSourceReader() throws Exception
	{
		Reader sourceReader = mock(Reader.class);
		Reader tokenisingReader = new TokenReplacingReader( APP_NAME, mockTokenFinder, sourceReader );
		tokenisingReader.close();
		verify(sourceReader, times(1)).close();
	}

    @Test
    public void tokenReplacingReaderCanBeConfiguredToIgnoreFailedReplacementsAndIncludeOriginalToken() throws Exception
    {
        Reader tokenisingReader = new TokenReplacingReader( APP_NAME, mockTokenFinder, new StringReader("@EXCEPTION.THROWING.TOKEN@"), mockTokenReplacementHandler );
        String replacedContent = IOUtils.toString(tokenisingReader);
        assertEquals("@EXCEPTION.THROWING.TOKEN@", replacedContent);
    }

    @Test
    public void ignoredFailedReplacementsDoNotCauseOtherTokensTobeReplacedBefore() throws Exception
    {
        Reader tokenisingReader = new TokenReplacingReader( APP_NAME, mockTokenFinder, new StringReader("@A.TOKEN@@EXCEPTION.THROWING.TOKEN@"), mockTokenReplacementHandler );
        String replacedContent = IOUtils.toString(tokenisingReader);
        assertEquals("token replacement@EXCEPTION.THROWING.TOKEN@", replacedContent);
    }

    @Test
    public void ignoredFailedReplacementsDoNotCauseOtherTokensTobeReplacedAfterwards() throws Exception
    {
        Reader tokenisingReader = new TokenReplacingReader( APP_NAME, mockTokenFinder, new StringReader("@EXCEPTION.THROWING.TOKEN@@A.TOKEN@"), mockTokenReplacementHandler );
        String replacedContent = IOUtils.toString(tokenisingReader);
        assertEquals("@EXCEPTION.THROWING.TOKEN@token replacement", replacedContent);
    }

    @Test
    public void ignoredFailedReplacementsDoNotCauseOtherTokensTobeReplacedBeforeAndAfterwards() throws Exception
    {
        Reader tokenisingReader = new TokenReplacingReader( APP_NAME, mockTokenFinder, new StringReader("@A.TOKEN@@EXCEPTION.THROWING.TOKEN@@A.TOKEN@"), mockTokenReplacementHandler );
        String replacedContent = IOUtils.toString(tokenisingReader);
        assertEquals("token replacement@EXCEPTION.THROWING.TOKEN@token replacement", replacedContent);
    }

    @Test
    public void tokenReplacementHandlerIsNotifiedAndCanCauseTheFailedReplacementToBeIgnored() throws Exception
    {
        Reader tokenisingReader = new TokenReplacingReader( APP_NAME, mockTokenFinder, new StringReader("@EXCEPTION.THROWING.TOKEN@"), mockTokenReplacementHandler );
        String replacedContent = IOUtils.toString(tokenisingReader);
        assertEquals("@EXCEPTION.THROWING.TOKEN@", replacedContent);
        verify(mockTokenReplacementHandler, times(1)).handleNoTokenFound( eq(APP_NAME), eq("EXCEPTION.THROWING.TOKEN"), any(TokenReplacementException.class) );
    }
    
    @Test
    public void brjsTokensAreHandledByTheBrjsTokenFinder() throws Exception
    {
    	Reader tokenisingReader = new TokenReplacingReader( APP_NAME, brjsTokenFinder, mockTokenFinder, new StringReader("@BRJS.TOKEN@"), mockTokenReplacementHandler );
        String replacedContent = IOUtils.toString(tokenisingReader);
        assertEquals("brjs token replacement", replacedContent);
        verify(mockTokenFinder, times(0)).findTokenValue(any(String.class));
        verify(brjsTokenFinder, times(1)).findTokenValue("BRJS.TOKEN");
    }
    
    @Test
    public void brjsTokensAreHandledTheSameAsUserTokens() throws Exception
    {
    	Reader tokenisingReader;
    	
    	tokenisingReader = new TokenReplacingReader( APP_NAME, brjsTokenFinder, mockTokenFinder, new StringReader("@BRJS.TOKEN@"), mockTokenReplacementHandler );
        assertEquals("brjs token replacement", IOUtils.toString(tokenisingReader));
        
        tokenisingReader = new TokenReplacingReader( APP_NAME, brjsTokenFinder, mockTokenFinder, new StringReader("@BRJS.EMPTY.TOKEN@"), mockTokenReplacementHandler );
        assertEquals("", IOUtils.toString(tokenisingReader));
        
        tokenisingReader = new TokenReplacingReader( APP_NAME, brjsTokenFinder, mockTokenFinder, new StringReader("@BRJS.NULL.TOKEN@"), mockTokenReplacementHandler );
        assertEquals("", IOUtils.toString(tokenisingReader));
    }
    
    @Test
    public void invalidBrjsTokensAlwaysThrowAnExceptionAndArentHandledByTheNoReplacementHandler() throws Exception {
    	exception.expect(IllegalArgumentException.class);
        exception.expectMessage( String.format(TokenReplacingReader.NO_BRJS_TOKEN_FOUND_MESSAGE, "BRJS.EXCEPTION.THROWING.TOKEN") );
        
    	Reader tokenisingReader = new TokenReplacingReader( APP_NAME, brjsTokenFinder, mockTokenFinder, new StringReader("@BRJS.EXCEPTION.THROWING.TOKEN@"), mockTokenReplacementHandler );
        IOUtils.toString(tokenisingReader);
        verify(mockTokenReplacementHandler, times(0)).handleNoTokenFound( eq(APP_NAME), any(String.class), any(TokenReplacementException.class) );
    }
    
    @Test // the servlet filter does not have BRJS at runtime so can't depend on any BRJS classes or tokens 
    public void exceptionIsThrownIfBrjsTokenHasBeenUsedButNotBrjsTokenFinderIsConfigured() throws Exception
    {
    	exception.expect(IllegalArgumentException.class);
        exception.expectMessage(TokenReplacingReader.NO_BRJS_TOKEN_CONFIGURED_MESSAGE);
        
    	Reader tokenisingReader = new TokenReplacingReader( APP_NAME, mockTokenFinder, new StringReader("@BRJS.TOKEN@"), mockTokenReplacementHandler );
        IOUtils.toString(tokenisingReader);
    }
    
    @SuppressWarnings("unchecked")
	@Test
    public void userTokensCannotOverrideBrjsTokens() throws Exception {
    	when(brjsTokenFinder.findTokenValue("BRJS.NO.OVERRIDING.TOKEN")).thenThrow(TokenReplacementException.class);
		when(mockTokenFinder.findTokenValue("BRJS.NO.OVERRIDING.TOKEN")).thenReturn("BRJS no overriding replacement" );
    	
    	exception.expect(IllegalArgumentException.class);
        exception.expectMessage( String.format(TokenReplacingReader.NO_BRJS_TOKEN_FOUND_MESSAGE, "BRJS.NO.OVERRIDING.TOKEN") );
        
    	Reader tokenisingReader = new TokenReplacingReader( APP_NAME, brjsTokenFinder, mockTokenFinder, new StringReader("@BRJS.NO.OVERRIDING.TOKEN@"), mockTokenReplacementHandler );
        IOUtils.toString(tokenisingReader);
        verify(mockTokenReplacementHandler, times(0)).handleNoTokenFound( eq(APP_NAME), any(String.class), any(TokenReplacementException.class) );
    }
    
    @Test
	public void asciiCharactersArentCorrupt() throws Exception
	{
    	Reader tokenisingReader = new TokenReplacingReader( APP_NAME, mockTokenFinder, new StringReader("@A.TOKEN@ $£€") );
		String replacedContent = IOUtils.toString( tokenisingReader );
		assertEquals("token replacement $£€", replacedContent);
		verify(mockTokenFinder, times(1)).findTokenValue("A.TOKEN");
	}
    
}
