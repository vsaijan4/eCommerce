package com.ecommerce.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.log4j.Logger;

import com.ecommerce.data.design.IMaxIDDataAccess;
import com.ecommerce.util.FileUtil;

public class MaxIDFileSystemImpl implements IMaxIDDataAccess {

	private static final String filePath = FileUtil.getRootPath() + "MaxID.txt";
	private final Logger logger = Logger.getLogger(this.getClass().getSimpleName());
	private static File file = null;
	private static FileReader fileRd = null;
	private static BufferedReader buffRd = null;
	private static BufferedWriter buffWr = null;
	private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
	private final Lock write = readWriteLock.writeLock();
	
	@Override
	public synchronized long get(int modelType) throws IOException, InterruptedException {
		file = new File(filePath);
		while(readWriteLock.isWriteLocked()) {
			Thread.sleep(100);
		}
		write.lock();
		if (!file.exists()) {
			logger.info("MaxID.txt file does not exist!");
			return -1;
		}
		
		long currId = -1;
		try {
			fileRd = new FileReader(file.getAbsoluteFile());
			buffRd = new BufferedReader(fileRd);
			String content[] = buffRd.readLine().split(":");

			int modelTypeVal = Integer.parseInt(content[0]);
			while(modelTypeVal != modelType) {
				content = buffRd.readLine().split(":");
				modelTypeVal = Integer.parseInt(content[0]);
			}
			currId = Long.parseLong(content[1]);
		} finally {
			buffRd.close();
			fileRd.close();
		}
		currId++;
		update(modelType, currId);

		write.unlock();
		return currId;
	}

	@Override
	public synchronized boolean update(int modelType, long id) throws IOException, InterruptedException {
		File maxFile = new File(filePath);
		String tempMaxFilePath = FileUtil.getRootPath() + "tempMaxID.txt";
		File tempMaxFile = new File(tempMaxFilePath);

		buffRd = new BufferedReader(new FileReader(maxFile));
		buffWr = new BufferedWriter(new FileWriter(tempMaxFile));

		String lineToRemove = modelType + ":";
		String currentLine;

		while ((currentLine = buffRd.readLine()) != null) {
			String trimmedLine = currentLine.trim();
			if (trimmedLine.startsWith(lineToRemove)) {
				currentLine = modelType + ":" + id;
			}
			buffWr.write(currentLine + System.getProperty("line.separator"));
		}
		buffWr.close();
		buffRd.close();
		maxFile.delete();
		tempMaxFile.renameTo(maxFile);

		return true;
	}
}
