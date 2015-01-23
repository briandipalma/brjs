package org.bladerunnerjs.utility.deps;

import java.io.IOException;
import java.io.Reader;
import java.util.Collections;
import java.util.List;

import org.bladerunnerjs.aliasing.AliasDefinition;
import org.bladerunnerjs.api.Asset;
import org.bladerunnerjs.api.AssetLocation;
import org.bladerunnerjs.api.LinkedAsset;
import org.bladerunnerjs.memoization.MemoizedFile;
import org.bladerunnerjs.model.BundlableNode;
import org.bladerunnerjs.model.exception.ModelOperationException;
import org.bladerunnerjs.utility.PrimaryRequirePathUtility;

public class AliasAsset implements LinkedAsset {
	private final AliasDefinition alias;
	
	public AliasAsset(AliasDefinition alias) {
		this.alias = alias;
	}
	
	@Override
	public Reader getReader() throws IOException {
		return null;
	}
	
	@Override
	public AssetLocation assetLocation() {
		return null;
	}
	
	@Override
	public MemoizedFile dir() {
		return null;
	}
	
	@Override
	public String getAssetName() {
		return alias.getName();
	}
	
	@Override
	public String getAssetPath() {
		return "alias!" + alias.getName();
	}
	
	@Override
	public List<Asset> getDependentAssets(BundlableNode bundlableNode) throws ModelOperationException {
		return null;
	}
	
	@Override
	public List<String> getAliasNames() throws ModelOperationException {
		return null;
	}
	
	@Override
	public List<String> getRequirePaths() {
		return Collections.emptyList();
	}
	
	@Override
	public String getPrimaryRequirePath() {
		return PrimaryRequirePathUtility.getPrimaryRequirePath(this);
	}
}
