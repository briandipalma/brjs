package org.bladerunnerjs.plugin.bundlers.namespacedjs;

import java.io.IOException;
import java.io.Reader;

import org.bladerunnerjs.utility.reader.AssetReaderFactory;
import org.bladerunnerjs.utility.reader.JsCodeBlockStrippingDependenciesReader;
import org.bladerunnerjs.utility.reader.JsCommentStrippingReader;
import org.bladerunnerjs.utility.reader.JsModuleExportsStrippingReader;

// TODO: merge this class with NamespacedJsPostExportDefineTimeDependenciesReader
public class NamespacedJsPreExportDefineTimeDependenciesReader extends Reader {
	
	private Reader namespacedJsPreExportDefineTimeDependenciesReader;
	
	public NamespacedJsPreExportDefineTimeDependenciesReader(NamespacedJsSourceModule sourceModule) throws IOException
	{
		Reader commentStrippingReader = new JsCommentStrippingReader(sourceModule.assetContainer().root(), sourceModule.getUnalteredContentReader(), false);
		Reader codeBlockStrippingReader = new JsCodeBlockStrippingDependenciesReader(sourceModule.assetContainer().root(), commentStrippingReader);
		namespacedJsPreExportDefineTimeDependenciesReader = new JsModuleExportsStrippingReader(sourceModule.assetContainer().root(), codeBlockStrippingReader, true);
	}
	
	@Override
	public int read(char[] cbuf, int off, int len) throws IOException
	{
		return namespacedJsPreExportDefineTimeDependenciesReader.read(cbuf, off, len);
	}

	@Override
	public void close() throws IOException
	{
		namespacedJsPreExportDefineTimeDependenciesReader.close();
	}
	
	static class Factory implements AssetReaderFactory {
		
		private NamespacedJsSourceModule sourceModule;

		public Factory(NamespacedJsSourceModule sourceModule)
		{
			this.sourceModule = sourceModule;
		}
		
		@Override
		public Reader createReader() throws IOException {
			return new NamespacedJsPreExportDefineTimeDependenciesReader(sourceModule);
		}
	}
	
}
