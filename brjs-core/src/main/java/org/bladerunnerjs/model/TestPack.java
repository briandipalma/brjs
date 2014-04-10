package org.bladerunnerjs.model;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.naming.InvalidNameException;

import org.bladerunnerjs.aliasing.aliases.AliasesFile;
import org.bladerunnerjs.model.engine.NamedNode;
import org.bladerunnerjs.model.engine.Node;
import org.bladerunnerjs.model.engine.NodeItem;
import org.bladerunnerjs.model.engine.NodeMap;
import org.bladerunnerjs.model.engine.RootNode;
import org.bladerunnerjs.model.exception.modelupdate.ModelUpdateException;
import org.bladerunnerjs.plugin.AssetPlugin;
import org.bladerunnerjs.utility.NameValidator;
import org.bladerunnerjs.utility.filemodification.NodeFileModifiedChecker;


public class TestPack extends AbstractBundlableNode implements NamedNode
{
	private final NodeItem<DirNode> tests = new NodeItem<>(DirNode.class, "tests");
	private final NodeItem<DirNode> testSource = new NodeItem<>(DirNode.class, "src-test");
	private AliasesFile aliasesFile;
	private String name;
	
	private NodeFileModifiedChecker sourceModulesFileModifiedChecker = new NodeFileModifiedChecker(this);
	private Set<SourceModule> sourceModules = null;
	
	public TestPack(RootNode rootNode, Node parent, File dir, String name)
	{
		super(rootNode, parent, dir);
		this.name = name;
		
		// TODO: we should never call registerInitializedNode() from a non-final class
		registerInitializedNode();
	}
	
	public static NodeMap<TestPack> createNodeSet(RootNode rootNode)
	{
		return new NodeMap<>(rootNode, TestPack.class, "", null);
	}
	
	@Override
	public List<LinkedAsset> getSeedFiles() 
	{
		List<LinkedAsset> seedFiles = new ArrayList<>();
		
		for(AssetPlugin assetPlugin : (root()).plugins().assetProducers()) {
			for(AssetLocation assetLocation : assetLocations()) {
				if(isTestAssetLocation(assetLocation)) {
					seedFiles.addAll(assetPlugin.getTestSourceModules(assetLocation));
				}
			}
		}
		
		return seedFiles;
	}
	
	@Override
	public String requirePrefix()
	{
		return "";
	}
	
	@Override
	public String namespace() {
		return ((AssetContainer) parentNode().parentNode()).namespace(); //TOOD: refactor this
	}
	
	@Override
	public boolean isNamespaceEnforced() {
		return false;
	}
	
	@Override
	public List<AssetContainer> assetContainers()
	{
		List<AssetContainer> assetContainers = new ArrayList<>(((AssetContainer) parentNode().parentNode()).scopeAssetContainers());
		assetContainers.add(this);
		
		return assetContainers;
	}
	
	@Override
	public Set<SourceModule> sourceModules() {
		if(sourceModulesFileModifiedChecker.hasChangedSinceLastCheck() || (sourceModules == null)) {
			sourceModules = new LinkedHashSet<SourceModule>();
			
			for(AssetPlugin assetPlugin : (root()).plugins().assetProducers()) {
				for (AssetLocation assetLocation : assetLocations())
				{
					if ( !isTestAssetLocation(assetLocation) )
					{
						sourceModules.addAll(assetPlugin.getSourceModules(assetLocation));
					}
				}
			}
		}
		
		return sourceModules;
	}
	
	@Override
	public void addTemplateTransformations(Map<String, String> transformations) throws ModelUpdateException
	{
	}
	
	@Override
	public String getName()
	{
		return name;
	}
	
	@Override
	public boolean isValidName()
	{
		return NameValidator.isValidDirectoryName(name);
	}
	
	@Override
	public void assertValidName() throws InvalidNameException
	{
		NameValidator.assertValidDirectoryName(this);
	}
	
	@Override
	public String getTemplateName()
	{
		return parentNode().parentNode().getClass().getSimpleName().toLowerCase() + "-" + name;
	}
	
	public AliasesFile aliasesFile()
	{
		if(aliasesFile == null) {
			aliasesFile = new AliasesFile(dir(), "resources/aliases.xml", this);
		}
		
		return aliasesFile;
	}
	
	public DirNode testSource()
	{
		return item(testSource);
	}
	
	public DirNode tests()
	{
		return item(tests);
	}
	
	
	private boolean isTestAssetLocation(AssetLocation assetLocation)
	{
		return assetLocation instanceof TestAssetLocation || assetLocation instanceof ChildTestAssetLocation;
	}
	
}
