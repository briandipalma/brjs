package org.bladerunnerjs.plugin.plugins.bundlers.xml;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

import org.bladerunnerjs.api.AssetLocation;
import org.bladerunnerjs.memoization.MemoizedFile;
import org.bladerunnerjs.memoization.MemoizedValue;
import org.bladerunnerjs.model.LinkedFileAsset;

public class XMLAsset extends LinkedFileAsset {
	private final MemoizedValue<List<String>> requirePaths;
	
	public XMLAsset(MemoizedFile assetFile, AssetLocation assetLocation) {
		super(assetFile, assetLocation);
		requirePaths = new MemoizedValue<>("XMLAsset.requirePaths", assetLocation.root(), assetFile);
	}
	
	@Override
	public List<String> getRequirePaths() {
		try {
			return requirePaths.value(() -> {
				Reader reader = getReader();
				XMLIdExtractor extractor = new XMLIdExtractor();
				return extractor.getXMLIds(reader);
			});
		}
		catch(IOException e) {
			throw new RuntimeException(e);
		}
	}
}
