package org.bladerunnerjs.testing.specutility;

import java.util.List;

import org.bladerunnerjs.appserver.ServletModelAccessor;
import org.bladerunnerjs.model.BRJS;
import org.bladerunnerjs.plugin.AssetPlugin;
import org.bladerunnerjs.plugin.BundlerTagHandlerPlugin;
import org.bladerunnerjs.plugin.CommandPlugin;
import org.bladerunnerjs.plugin.BundlerContentPlugin;
import org.bladerunnerjs.plugin.ContentPlugin;
import org.bladerunnerjs.plugin.MinifierPlugin;
import org.bladerunnerjs.plugin.ModelObserverPlugin;
import org.bladerunnerjs.plugin.Plugin;
import org.bladerunnerjs.plugin.TagHandlerPlugin;
import org.bladerunnerjs.plugin.proxy.VirtualProxyAssetPlugin;
import org.bladerunnerjs.plugin.proxy.VirtualProxyBundlerTagHandlerPlugin;
import org.bladerunnerjs.plugin.proxy.VirtualProxyCommandPlugin;
import org.bladerunnerjs.plugin.proxy.VirtualProxyBundlerContentPlugin;
import org.bladerunnerjs.plugin.proxy.VirtualProxyContentPlugin;
import org.bladerunnerjs.plugin.proxy.VirtualProxyMinifierPlugin;
import org.bladerunnerjs.plugin.proxy.VirtualProxyModelObserverPlugin;
import org.bladerunnerjs.plugin.proxy.VirtualProxyTagHandlerPlugin;
import org.bladerunnerjs.plugin.utility.PluginLoader;
import org.bladerunnerjs.testing.specutility.engine.BuilderChainer;
import org.bladerunnerjs.testing.specutility.engine.NodeBuilder;
import org.bladerunnerjs.testing.specutility.engine.SpecTest;
import org.mockito.Mockito;


public class BRJSBuilder extends NodeBuilder<BRJS> {
	private BRJS brjs;
	
	public BRJSBuilder(SpecTest modelTest, BRJS brjs) {
		super(modelTest, brjs);
		this.brjs = brjs;
	}
	
	//TODO: look at brjs is null - commands must be added before BRJS is created
	
	public BuilderChainer hasBeenPopulated() throws Exception {
		brjs.populate();
		
		return builderChainer;
	}

	public BuilderChainer hasCommands(CommandPlugin... commands)
	{
		verifyBrjsIsSet();
		verifyPluginsUnitialized(specTest.pluginLocator.pluginCommands);
		
		for(CommandPlugin command : commands)
		{
			specTest.pluginLocator.pluginCommands.add( new VirtualProxyCommandPlugin(command) );
		}
		
		return builderChainer;
	}
	
	public BuilderChainer hasModelObservers(ModelObserverPlugin... modelObservers)
	{
		verifyBrjsIsSet();
		verifyPluginsUnitialized(specTest.pluginLocator.modelObservers);
		
		for(ModelObserverPlugin modelObserver : modelObservers)
		{
			specTest.pluginLocator.modelObservers.add( new VirtualProxyModelObserverPlugin(modelObserver) );
		}
		
		return builderChainer;
	}
	
	public BuilderChainer hasBundlerContentPlugins(BundlerContentPlugin... contentPlugins)
	{
		verifyBrjsIsSet();
		
		for(BundlerContentPlugin contentPlugin : contentPlugins)
		{
			specTest.pluginLocator.bundlerContentPlugins.add( new VirtualProxyBundlerContentPlugin(contentPlugin) );
		}
		
		return builderChainer;
	}
	
	public BuilderChainer hasMinifiers(MinifierPlugin... minifyPlugins)
	{
		verifyBrjsIsSet();
		verifyPluginsUnitialized(specTest.pluginLocator.minifiers);
		
		for(MinifierPlugin minifierPlugin : minifyPlugins)
		{
			specTest.pluginLocator.minifiers.add( new VirtualProxyMinifierPlugin(minifierPlugin) );
		}
		
		return builderChainer;
	}
	
	public BuilderChainer hasTagPlugins(TagHandlerPlugin... tagHandlers)
	{
		verifyBrjsIsSet();
		verifyPluginsUnitialized(specTest.pluginLocator.tagHandlers);
		
		for(TagHandlerPlugin tagHandler : tagHandlers)
		{
			specTest.pluginLocator.tagHandlers.add( new VirtualProxyTagHandlerPlugin(tagHandler) );
		}
		
		return builderChainer;
	}
	
	public BuilderChainer automaticallyFindsCommands()
	{
		verifyBrjsIsSet();
		verifyPluginsUnitialized(specTest.pluginLocator.pluginCommands);
		
		specTest.pluginLocator.pluginCommands.addAll( PluginLoader.createPluginsOfType(Mockito.mock(BRJS.class), CommandPlugin.class, VirtualProxyCommandPlugin.class) );
		
		return builderChainer;
	}
	
	public BuilderChainer automaticallyFindsModelObservers()
	{
		verifyBrjsIsSet();
		verifyPluginsUnitialized(specTest.pluginLocator.modelObservers);
		
		specTest.pluginLocator.modelObservers.addAll( PluginLoader.createPluginsOfType(Mockito.mock(BRJS.class), ModelObserverPlugin.class, VirtualProxyModelObserverPlugin.class) );
		
		return builderChainer;
	}
	
	public BuilderChainer automaticallyFindsContentPlugins() 
	{
		verifyBrjsIsSet();
		verifyPluginsUnitialized(specTest.pluginLocator.contentPlugins);
		
		specTest.pluginLocator.contentPlugins.addAll( PluginLoader.createPluginsOfType(Mockito.mock(BRJS.class), ContentPlugin.class, VirtualProxyContentPlugin.class) );
		automaticallyFindsBundlerContentPlugins();
		
		return builderChainer;
	}
	
	public BuilderChainer automaticallyFindsBundlerContentPlugins() 
	{
		verifyBrjsIsSet();
		verifyPluginsUnitialized(specTest.pluginLocator.bundlerContentPlugins);
		
		specTest.pluginLocator.bundlerContentPlugins.addAll( PluginLoader.createPluginsOfType(Mockito.mock(BRJS.class), BundlerContentPlugin.class, VirtualProxyBundlerContentPlugin.class) );
		
		return builderChainer;
	}
	
	public BuilderChainer automaticallyFindsTagHandlers() 
	{
		verifyBrjsIsSet();
		verifyPluginsUnitialized(specTest.pluginLocator.tagHandlers);
		
		specTest.pluginLocator.tagHandlers.addAll( PluginLoader.createPluginsOfType(Mockito.mock(BRJS.class), TagHandlerPlugin.class, VirtualProxyTagHandlerPlugin.class) );
		automaticallyFindsBundlerTagHandlers();
		
		return builderChainer;
	}
	
	public BuilderChainer automaticallyFindsBundlerTagHandlers() 
	{
		verifyBrjsIsSet();
		verifyPluginsUnitialized(specTest.pluginLocator.bundlerTagHandlers);
		
		specTest.pluginLocator.bundlerTagHandlers.addAll( PluginLoader.createPluginsOfType(Mockito.mock(BRJS.class), BundlerTagHandlerPlugin.class, VirtualProxyBundlerTagHandlerPlugin.class) );
		
		return builderChainer;
	}
	
	public BuilderChainer automaticallyFindsAssetProducers() {
		verifyBrjsIsSet();
		verifyPluginsUnitialized(specTest.pluginLocator.assetPlugins);
		
		specTest.pluginLocator.assetPlugins.addAll( PluginLoader.createPluginsOfType(Mockito.mock(BRJS.class), AssetPlugin.class, VirtualProxyAssetPlugin.class) );
		
		return builderChainer;
	}

	public BuilderChainer automaticallyFindsBundlers()
	{
		automaticallyFindsBundlerContentPlugins();
		automaticallyFindsBundlerTagHandlers();
		automaticallyFindsAssetProducers();
		
		return builderChainer;
	}
	
	public BuilderChainer automaticallyFindsMinifiers() 
	{
		verifyBrjsIsSet();
		verifyPluginsUnitialized(specTest.pluginLocator.minifiers);
		
		specTest.pluginLocator.minifiers.addAll( PluginLoader.createPluginsOfType(Mockito.mock(BRJS.class), MinifierPlugin.class, VirtualProxyMinifierPlugin.class) );
		
		return builderChainer;
	}
	
	@Override
	public BuilderChainer hasBeenCreated() throws Exception
	{
		brjs = specTest.createModel();
		specTest.brjs = brjs;
		this.node = brjs;
		
		super.hasBeenCreated();
		
		return builderChainer;
	}
	
	public BuilderChainer hasBeenAuthenticallyCreated() throws Exception
	{
		brjs = specTest.createNonTestModel();
		specTest.brjs = brjs;
		this.node = brjs;
		
		return builderChainer;
	}

	public BuilderChainer usedForServletModel()
	{
		ServletModelAccessor.reset();
		ServletModelAccessor.initializeModel(brjs);
		return builderChainer;
	}
	
	private void verifyBrjsIsSet()
	{
		if (specTest.brjs != null)
		{
			throw new RuntimeException("Plugins must be added to BRJS before it is created.");
		}
	}
	
	private <P extends Plugin> void verifyPluginsUnitialized(List<P> pluginList) {
		if(pluginList.size() > 0) {
			throw new RuntimeException("automaticallyFindsXXX() invoked after plug-ins have already been added.");
		}
	}
}
