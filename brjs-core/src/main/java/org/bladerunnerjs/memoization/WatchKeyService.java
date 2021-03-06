package org.bladerunnerjs.memoization;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.WatchKey;
import java.util.Map;


public interface WatchKeyService
{
	public WatchKey waitForEvents() throws InterruptedException;
	public Map<WatchKey,Path> createWatchKeysForDir(Path dirPath, boolean isNewlyDiscovered) throws IOException;
	public void close() throws IOException;	
}
