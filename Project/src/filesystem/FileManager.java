package filesystem;

import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

public class FileManager {


	public static ConcurrentHashMap<String, String> nameToFileID = new ConcurrentHashMap<String, String>();

	public static ConcurrentHashMap<String, BackedUpFile> backedUpFiles = new ConcurrentHashMap<String, BackedUpFile>();


	public static ConcurrentHashMap<String, ConcurrentHashMap<Integer, Integer>> filesTrackReplication = new ConcurrentHashMap<String, ConcurrentHashMap<Integer, Integer>>();

	public static ConcurrentHashMap<String, ConcurrentHashMap<Integer, Chunk>> storedChunks = new ConcurrentHashMap<String, ConcurrentHashMap<Integer, Chunk>>();


	public static ConcurrentHashMap<String, HashSet<Integer>> chunksToSend = new ConcurrentHashMap<String, HashSet<Integer>>();


	public static float maxStorage = 6000; 


	public static void addBackedUpFile(BackedUpFile file){
		nameToFileID.put(file.getFilePath(), file.getFileID());
		backedUpFiles.put(file.getFileID(), file);
	}


	public static void addBackedUpFileChunk(String fileID, int chunkNo, long size, int desiredReplicationDeg){
		backedUpFiles.get(fileID).addChunk(chunkNo, size, desiredReplicationDeg);
	}


	public static boolean hasBackedUpFilePathName(String filePath){	
		return nameToFileID.containsKey(filePath);
	}


	public static boolean hasBackedUpFileID(String fileID){	
		return backedUpFiles.containsKey(fileID);
	}


	public static String getBackedUpFileID(String filePath) {
		return nameToFileID.get(filePath);
	}


	public static String getBackedUpFileName(String fileID) {
		return backedUpFiles.get(fileID).getFileName();
	}


	public static ConcurrentHashMap <Integer, Chunk> getChunksBackedUpFile(String fileID){
		return backedUpFiles.get(fileID).getChunks();
	}


	public static void deleteBackedUpFile(String filePath, String fileID){
		nameToFileID.remove(filePath);
		backedUpFiles.remove(fileID);
	}


	public static void addBackedUpChunkReplication(String fileID, int chunkNo){
		backedUpFiles.get(fileID).addReplication(chunkNo);
	}


	public static int getBackedUpChunkPerceivedReplication(String fileID, int chunkNo){
		return backedUpFiles.get(fileID).getChunkPerceivedReplication(chunkNo);
	}


	public static boolean hasStoredFileID(String fileID){
		return storedChunks.containsKey(fileID);
	}


	public static boolean hasStoredChunkNo(String fileID, int chunkNo){
		if(storedChunks.containsKey(fileID))
			if(storedChunks.get(fileID).containsKey(chunkNo))
				return true;			

		return false;
	}


	public static boolean hasPerceveidedLowerDesired(String fileID, int chunkNo){
		return storedChunks.get(fileID).get(chunkNo).hasPerceveidedLowerDesired();
	}


	public static int getStoredDesiredReplicationDeg(String fileID, int chunkNo){
		return storedChunks.get(fileID).get(chunkNo).getDesiredReplicationDeg();
	}


	public static void addStoredFile(String fileID){
		storedChunks.put(fileID, new ConcurrentHashMap<Integer, Chunk>());
	}


	public static void addStoredChunk(String fileID, int chunkNo, long size, int desiredReplicationDeg, int perceivedReplicationDeg){
		Chunk chunk = new Chunk(fileID, chunkNo, size, desiredReplicationDeg);
		chunk.setPerceivedReplicationDeg(perceivedReplicationDeg);
		storedChunks.get(fileID).put(chunkNo, chunk);
	}	


	public static void removeStoredFile(String fileID){
		storedChunks.remove(fileID);
	}


	public static void removeStoredChunk(String fileID, int chunkNo){
		storedChunks.get(fileID).remove(chunkNo);
	}


	public static ConcurrentHashMap<Integer, Chunk> getStoredChunks(String fileID){
		return storedChunks.get(fileID);
	}


	public static ConcurrentHashMap<String, ConcurrentHashMap<Integer, Chunk>>  getStoredFilesChunks(){
		return storedChunks;
	}


	public static boolean hasChunkToSend(String fileID, int chunkNo){
		if(chunksToSend.containsKey(fileID))
			if(chunksToSend.get(fileID).contains(chunkNo))
				return true;

		return false;
	}


	public static void addChunkToSend(String fileID, int chunkNo){
		if(chunksToSend.containsKey(fileID)){
			if(!chunksToSend.get(fileID).contains(chunkNo))
				chunksToSend.get(fileID).add(chunkNo);
		} else {
			chunksToSend.put(fileID, new HashSet<Integer>());
			chunksToSend.get(fileID).add(chunkNo);
		}
	}


	public static void removeChunkToSend(String fileID, int chunkNo){
		System.out.println(fileID+"   "+chunkNo);
		if(chunksToSend.containsKey(fileID)){
			if(chunksToSend.get(fileID).contains(chunkNo)){
				chunksToSend.get(fileID).remove(chunkNo);
			}
		}
	}


	public static int getPerceivedReplicationDeg(String fileID, int chunkNo){	
		if(filesTrackReplication.containsKey(fileID)){
			if(!filesTrackReplication.get(fileID).containsKey(chunkNo)){
				filesTrackReplication.get(fileID).put(chunkNo, 0);	
			} 
		} else {
			filesTrackReplication.put(fileID, new ConcurrentHashMap<Integer, Integer>());
			filesTrackReplication.get(fileID).put(chunkNo, 0);
		}

		return filesTrackReplication.get(fileID).get(chunkNo);
	}


	public static void updateBackedUpReplicationDeg(String fileID, int chunkNo) {

		/* Updates backed up files structure */
		addBackedUpChunkReplication(fileID, chunkNo);

		if(FileManager.filesTrackReplication.containsKey(fileID)){
			if(!FileManager.filesTrackReplication.get(fileID).containsKey(chunkNo)){
				FileManager.filesTrackReplication.get(fileID).put(chunkNo, 0);	
			} 
		} else {
			FileManager.filesTrackReplication.put(fileID, new ConcurrentHashMap<Integer, Integer>());
			FileManager.filesTrackReplication.get(fileID).put(chunkNo, 0);
		}

		int chunkrep = FileManager.filesTrackReplication.get(fileID).get(chunkNo);
		FileManager.filesTrackReplication.get(fileID).put(chunkNo, chunkrep+1);
	}
	
		public static boolean hasEnoughStorage(float space){

		float chunkSpace = (float)space/1000;

		if((getUsedStorage()+chunkSpace) > maxStorage)
			return false;
		else
			return true;
	}

	public static float getUsedStorage(){
		float storage = 0;

		for(String fileID: storedChunks.keySet())
			for(Chunk chunk: storedChunks.get(fileID).values())
				storage += chunk.getSize();

		return storage;
	}


	public static void updateStoredReplicationDeg(String fileID, int chunkNo) {
		if(FileManager.filesTrackReplication.containsKey(fileID)){
			if(!FileManager.filesTrackReplication.get(fileID).containsKey(chunkNo)){
				FileManager.filesTrackReplication.get(fileID).put(chunkNo, 0);	
			} 
		} else {
			FileManager.filesTrackReplication.put(fileID, new ConcurrentHashMap<Integer, Integer>());
			FileManager.filesTrackReplication.get(fileID).put(chunkNo, 0);
		}

		int chunkrep = FileManager.filesTrackReplication.get(fileID).get(chunkNo);
		FileManager.filesTrackReplication.get(fileID).put(chunkNo, chunkrep+1);

		if(FileManager.hasStoredFileID(fileID))
			if(FileManager.hasStoredChunkNo(fileID, chunkNo))
				FileManager.storedChunks.get(fileID).get(chunkNo).addPerceivedReplicationDeg();
	}


	public static void reduceReplicationDeg(String fileID, int chunkNo){
		if (FileManager.hasBackedUpFileID(fileID))
			backedUpFiles.get(fileID).reduceReplication(chunkNo);	

		if(FileManager.filesTrackReplication.containsKey(fileID)){
			if(!FileManager.filesTrackReplication.get(fileID).containsKey(chunkNo)){
				return;	
			} 
		} else {
			return;
		}

		int chunkrep = FileManager.filesTrackReplication.get(fileID).get(chunkNo);
		FileManager.filesTrackReplication.get(fileID).put(chunkNo, chunkrep-1);

		if(FileManager.hasStoredFileID(fileID))
			if(FileManager.hasStoredChunkNo(fileID, chunkNo))
				FileManager.storedChunks.get(fileID).get(chunkNo).reducePerceivedReplicationDeg();		
	}




	public static float getMaxStorage(){
		return maxStorage;
	}


	public static String getBackedUpToString(){
		String state="";

		if(backedUpFiles.size() > 0){
			state += "##### BACKED UP FILES #####";

			for (BackedUpFile file: backedUpFiles.values())
				state += "\n\n"+file;
		}	

		return state;
	}


	public static String getStoredChunksToString(){
		String state="";

		if(storedChunks.size() > 0){
			state += "\n\n##### STORED CHUNKS #####";

			for(String fileID: storedChunks.keySet()){
				if(storedChunks.get(fileID).size() > 0){
					state += "\n\nFILE ID: "+fileID;

					for(Chunk chunk: storedChunks.get(fileID).values())				
						state += "\nID: "+chunk.getNumber()+
						"  |  SIZE: "+chunk.getSize()+
						" KBytes  |  PERCEIVED REPLICATION DEGREE: "+chunk.getPerceivedReplicationDeg();
				}				
			}			
		}

		return state;
	}


	public static String getState(){
		String space = "\n\nMAX STORAGE CAPACITY: "+maxStorage+" KBytes  |  USED STORAGE: "+getUsedStorage()+" KBytes";

		return getBackedUpToString()+getStoredChunksToString()+space;
	}
}
