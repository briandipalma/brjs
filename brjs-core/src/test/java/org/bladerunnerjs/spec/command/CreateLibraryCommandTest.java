package org.bladerunnerjs.spec.command;

import static org.bladerunnerjs.model.engine.AbstractNode.Messages.*;
import static org.bladerunnerjs.plugin.plugins.commands.standard.CreateLibraryCommand.Messages.*;

import org.bladerunnerjs.model.App;
import org.bladerunnerjs.model.JsLib;
import org.bladerunnerjs.model.AppJsLib;
import org.bladerunnerjs.model.exception.command.ArgumentParsingException;
import org.bladerunnerjs.model.exception.command.CommandArgumentsException;
import org.bladerunnerjs.model.exception.command.NodeAlreadyExistsException;
import org.bladerunnerjs.model.exception.command.NodeDoesNotExistException;
import org.bladerunnerjs.model.exception.name.InvalidDirectoryNameException;
import org.bladerunnerjs.plugin.plugins.commands.standard.CreateLibraryCommand;
import org.bladerunnerjs.testing.specutility.engine.SpecTest;
import org.junit.Before;
import org.junit.Test;


public class CreateLibraryCommandTest extends SpecTest {
	App app;
	JsLib lib;
	JsLib badLib;
	
	@Before
	public void initTestObjects() throws Exception
	{
		given(brjs).hasCommands(new CreateLibraryCommand())
			.and(brjs).hasBeenCreated();
			app = brjs.app("app");
			lib = app.jsLib("lib");
			badLib = app.jsLib("lib#$@/");
	}
	
	@Test
	public void exceptionIsThrownIfThereAreTooFewArguments() throws Exception {
		when(brjs).runCommand("create-library", "a");
		then(exceptions).verifyException(ArgumentParsingException.class, unquoted("Parameter 'new-library-name' is required"))
			.whereTopLevelExceptionIs(CommandArgumentsException.class);
	}
	
	@Test
	public void exceptionIsThrownIfThereAreTooManyArguments() throws Exception {
		when(brjs).runCommand("create-library", "a", "b", "c");
		then(exceptions).verifyException(ArgumentParsingException.class, unquoted("Unexpected argument: c"))
			.whereTopLevelExceptionIs(CommandArgumentsException.class);
	}
	
	@Test
	public void exceptionIsThrownIfTheLibraryNameIsNotAValidDirectoryName() throws Exception {
		given(app).hasBeenCreated()
			.and(logging).enabled();
		when(brjs).runCommand("create-library", "app", "lib#$@/");
		then(logging).errorMessageReceived(NODE_CREATION_FAILED_LOG_MSG, AppJsLib.class.getSimpleName(), badLib.dir().getPath())
			.and(exceptions).verifyException(InvalidDirectoryNameException.class, "lib#$@/", badLib.dir().getPath())
			.whereTopLevelExceptionIs(CommandArgumentsException.class);
	}
	
	@Test
	public void exceptionIsThrownIfTheAppDoesntExist() throws Exception {
		when(brjs).runCommand("create-library", "app", "lib");
		then(exceptions).verifyException(NodeDoesNotExistException.class, unquoted(app.getClass().getSimpleName()))
			.whereTopLevelExceptionIs(CommandArgumentsException.class);
	}
	
	@Test
	public void exceptionIsThrownIfTheLibAlreadyExists() throws Exception {
		given(app).hasBeenCreated()
			.and(lib).hasBeenCreated();
		when(brjs).runCommand("create-library", "app", "lib");
		then(exceptions).verifyException(NodeAlreadyExistsException.class, unquoted(lib.getClass().getSimpleName()));
	}
	
	@Test
	public void libIsCreatedWhenAllArgumentsAreValid() throws Exception {
		given(app).hasBeenCreated();
		when(brjs).runCommand("create-library", "app", "lib");
		then(lib).dirExists()
			.and(output).containsLine( LIBRARY_CREATE_SUCCESS_CONSOLE_MSG, "lib" )
			.and(output).containsLine( LIBRARY_PATH_CONSOLE_MSG, lib.dir() );
	}
	
	@Test
	public void thirdpartyLibCanBeCreatedUsingASwitch() throws Exception {
		given(app).hasBeenCreated();
		when(brjs).runCommand("create-library", "app", "lib", "-t", "thirdparty");
		then( app.jsLib("lib") ).dirExists()
			.and(output).containsLine( LIBRARY_CREATE_SUCCESS_CONSOLE_MSG, "lib" )
			.and(output).containsLine( LIBRARY_PATH_CONSOLE_MSG, app.appJsLib("lib").dir() );
	}
	
	@Test
	public void thirdpartyLibCanBeCreatedUsingALonghandSwitch() throws Exception {
		given(app).hasBeenCreated();
		when(brjs).runCommand("create-library", "app", "lib", "--type", "thirdparty");
		then( app.jsLib("lib") ).dirExists()
			.and(output).containsLine( LIBRARY_CREATE_SUCCESS_CONSOLE_MSG, "lib" )
			.and(output).containsLine( LIBRARY_PATH_CONSOLE_MSG, app.jsLib("lib").dir() );
	}
	
	@Test
	public void anExceptionIsThrownIfTheLibraryTypeIsInvalid() throws Exception {
		given(app).hasBeenCreated();
		when(brjs).runCommand("create-library", "app", "lib", "-t", "INVALID");
		then(exceptions).verifyFormattedException( CommandArgumentsException.class, INVALID_LIB_TYPE_MESSAGE, "INVALID", unquoted("br, thirdparty") );
	}
	
	@Test
	public void brLibrariesCanBeCreatedInTheAppEvenIfTheyAreAlreadyInTheSdk() throws Exception {
		given(app).hasBeenCreated()
			.and(brjs.sdkLib("lib")).hasBeenCreated();
		when(brjs).runCommand("create-library", "app", "lib");
		then(app.appJsLib("lib")).dirExists();
	}
	
	@Test
	public void thirdpartyLibrariesCanBeCreatedInTheAppEvenIfTheyAreAlreadyInTheSdk() throws Exception {
		given(app).hasBeenCreated()
			.and(brjs.sdkNonBladeRunnerLib("lib")).hasBeenCreated();
		when(brjs).runCommand("create-library", "app", "lib", "-t", "thirdparty");
		then(app.appJsLib("lib")).dirExists();
	}
	
	@Test
	public void commandIsAutomaticallyLoaded() throws Exception
	{
		given(brjs).hasBeenAuthenticallyCreated()
			.and(app).hasBeenCreated();
		when(brjs).runCommand("create-library", "app", "lib");
		then(exceptions).verifyNoOutstandingExceptions();
	}
}
