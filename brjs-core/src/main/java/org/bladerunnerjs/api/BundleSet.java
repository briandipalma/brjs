package org.bladerunnerjs.api;

import java.util.List;

public interface BundleSet {
	public BundlableNode bundlableNode();
	public List<LinkedAsset> seedAssets();
	public List<Asset> assets(String... prefixes);
	public <AT extends Asset> List<AT> getAssets(Class<? extends AT> assetType, List<String> prefixes);
	public List<Asset> getAssets(List<String> prefixes, List<Class<? extends Asset>> assetTypes);
	public List<SourceModule> getSourceModules();
	public <AT extends SourceModule> List<AT> getSourceModules(Class<? extends AT> assetType);
	public List<SourceModule> getSourceModules(List<Class<? extends SourceModule>> assetTypes);
}
