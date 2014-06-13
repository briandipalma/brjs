package org.bladerunnerjs.plugin.utility;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.bladerunnerjs.model.BRJS;
import org.bladerunnerjs.plugin.AssetLocationPlugin;
import org.bladerunnerjs.plugin.AssetPlugin;
import org.bladerunnerjs.plugin.CommandPlugin;
import org.bladerunnerjs.plugin.ContentPlugin;
import org.bladerunnerjs.plugin.MinifierPlugin;
import org.bladerunnerjs.plugin.ModelObserverPlugin;
import org.bladerunnerjs.plugin.OrderedPlugin;
import org.bladerunnerjs.plugin.Plugin;
import org.bladerunnerjs.plugin.PluginLocator;
import org.bladerunnerjs.plugin.TagHandlerPlugin;
import org.bladerunnerjs.plugin.utility.command.CommandList;

public class PluginAccessor {
	private final CommandList commandList;
	private final List<ContentPlugin> contentProviders;
	private final List<TagHandlerPlugin> tagHandlers;
	private final List<MinifierPlugin> minifiers;
	private final List<ModelObserverPlugin> modelObservers;
	private final List<AssetPlugin> assetProducers;
	private final List<AssetLocationPlugin> assetLocationProducers;
	
	public PluginAccessor(BRJS brjs, PluginLocator pluginLocator) {
		commandList = new CommandList(brjs, pluginLocator.getCommandPlugins());
		contentProviders = sort(pluginLocator.getContentPlugins());
		tagHandlers = pluginLocator.getTagHandlerPlugins();
		minifiers = pluginLocator.getMinifierPlugins();
		modelObservers = pluginLocator.getModelObserverPlugins();
		assetProducers = sort(pluginLocator.getAssetPlugins());
		assetLocationProducers = sort(pluginLocator.getAssetLocationPlugins());
	}
	
	public List<Plugin> allPlugins() {
		List<Plugin> plugins = new ArrayList<>();
		
		plugins.addAll(commands());
		plugins.addAll(contentProviders());
		plugins.addAll(tagHandlers());
		plugins.addAll(minifiers());
		plugins.addAll(modelObservers());
		plugins.addAll(assetProducers());
		plugins.addAll(assetLocationProducers());
		
		return plugins;
	}
	
	public CommandList commandList() {
		return commandList;
	}
	
	public List<CommandPlugin> commands() {
		return commandList.getPluginCommands();
	}
	
	public ContentPlugin contentProviderForLogicalPath(String logicalRequestpath)
	{
		String requestPrefix = logicalRequestpath.substring(0, logicalRequestpath.indexOf('/'));
		
		return contentProvider(requestPrefix);
	}
	
	public ContentPlugin contentProvider(String requestPrefix) {
		for (ContentPlugin contentPlugin : contentProviders()) {
			if(contentPlugin.getRequestPrefix().equals(requestPrefix)) {
				return contentPlugin;
			}
		}
		return null;
	}
	
	public List<ContentPlugin> contentProviders() {
		return contentProviders;
	}
	
	public List<ContentPlugin> contentProviders(String groupName) {
		List<ContentPlugin> contentProviders = new LinkedList<>();
		
		for (ContentPlugin contentPlugin : contentProviders()) {
			if (groupName.equals(contentPlugin.getCompositeGroupName())) {
				contentProviders.add(contentPlugin);
			}
		}
		
		return contentProviders;
	}
	
	public List<TagHandlerPlugin> tagHandlers() {
		return tagHandlers;
	}
	
	public TagHandlerPlugin tagHandler(String tagName) {
		for (TagHandlerPlugin tagHandler : tagHandlers()) {
			if(tagHandler.getTagName().equals(tagName)) {
				return tagHandler;
			}
		}
		return null;
	}
	
	public List<MinifierPlugin> minifiers() {
		return minifiers;
	}
	
	public MinifierPlugin minifier(String minifierSetting) {
		List<String> validMinificationSettings = new LinkedList<String>();
		MinifierPlugin pluginForMinifierSetting = null;
		
		for (MinifierPlugin minifierPlugin : minifiers()) {
			for (String setting : minifierPlugin.getSettingNames()) {
				validMinificationSettings.add(setting);
				
				if (setting.equals(minifierSetting)) {
					pluginForMinifierSetting = (pluginForMinifierSetting == null) ? minifierPlugin : pluginForMinifierSetting;
				}
			}
		}
		
		if (pluginForMinifierSetting != null) {
			return pluginForMinifierSetting;
		}
		
		throw new RuntimeException("No minifier plugin for minifier setting '" + minifierSetting + "'. Valid settings are: "
			+ StringUtils.join(validMinificationSettings, ", "));
	}
	
	public List<ModelObserverPlugin> modelObservers() {
		return modelObservers;
	}
	
	public List<AssetPlugin> assetProducers() {
		return assetProducers;
	}
	
	public List<AssetLocationPlugin> assetLocationProducers() {
		return assetLocationProducers;
	}
	
	public AssetPlugin assetProducer(Class<?> pluginClass ) {
		AssetPlugin result = null;
		List<AssetPlugin> assetProducers = assetProducers();
		for(AssetPlugin producer: assetProducers){
			Class<?> possiblePluginClass = producer.getPluginClass();
			if(possiblePluginClass.equals(pluginClass)){
				result =  producer;
				break;
			}
		}
		return result;
	}
	
	private <P extends OrderedPlugin> List<P> sort(List<P> plugins) {
		try {
			return PluginSorter.sort(plugins);
		}
		catch (PluginOrderingException | NonExistentPluginException e) {
			throw new RuntimeException(e);
		}
	}
}
