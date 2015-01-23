package org.bladerunnerjs.plugin;

import org.bladerunnerjs.api.Asset;
import org.bladerunnerjs.api.AssetLocation;
import org.bladerunnerjs.api.LinkedAsset;
import org.bladerunnerjs.api.SourceModule;
import org.bladerunnerjs.memoization.MemoizedFile;
import org.bladerunnerjs.model.AssetFileInstantationException;

/**
 * Asset plug-ins allow new implementations of {@link SourceModule}, {@link LinkedAsset} &amp; {@link Asset} to be supported within the model.
 */
public interface AssetPlugin extends OrderedPlugin {
	boolean canHandleAsset(MemoizedFile assetFile, AssetLocation assetLocation);
	Asset createAsset(MemoizedFile assetFile, AssetLocation assetLocation) throws AssetFileInstantationException;
}
