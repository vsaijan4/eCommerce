package com.ecommerce.data;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.log4j.Logger;

import com.ecommerce.data.design.ILookUpDataAccess;
import com.ecommerce.representation.*;
import com.ecommerce.util.FileUtil;

public class LookUpFileSystemImpl implements ILookUpDataAccess<Review> {

	private final Logger logger = Logger.getLogger(this.getClass().getSimpleName());

	private static final String lookUpFilePath = FileUtil.getRootPath() + "LookUp.txt";
	private static File file = null;
	private static FileReader fileRd = null;
	private static BufferedReader buffRd = null;
	private static FileWriter fileWr = null;
	private static BufferedWriter buffWr = null;
	private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
	private final Lock write = readWriteLock.writeLock();

	@Override
	public boolean create(Review review) throws InterruptedException, IOException {
		long reviewId = review.getId();
		long productId = review.getProductId();
		long userId = review.getUserId();
		
		return writeToFile(reviewId + "," + productId + "," + userId +"\n");
	}

	@Override
	public long read(Review review) throws InterruptedException, IOException {
		long reviewId = review.getId();
		long productId = -1;
		
		while(readWriteLock.isWriteLocked()) {
            Thread.sleep(100);
		}
		file = new File(lookUpFilePath);
		if (file.exists()) {
            try{
				fileRd = new FileReader(file.getAbsoluteFile());
				buffRd = new BufferedReader(fileRd);
				String fileContent = buffRd.readLine();
				if (fileContent != null) {
					long currReviewId;
					String content[] = fileContent.split(",");
					while (content != null) {
						currReviewId = Long.parseLong(content[0]);
						if (currReviewId == reviewId) {
							productId = Long.parseLong(content[1]);
							break;
						}
						fileContent = buffRd.readLine();
						if (fileContent != null) {
							content = fileContent.split(",");
						} else {
							break;
						}
					}
				}
			} finally {
                buffRd.close();
                fileRd.close();
			}
		} else {
			logger.error("LookUp.txt file does not exist!");
		}
		return productId;
	}

	@Override
	public List<Long> fetchAllReviewIDs(Product product) throws InterruptedException, IOException {
		long productId = product.getId();
		File file = new File(lookUpFilePath);
		List<Long> listOfReviewIDs = new ArrayList<Long>();

		while(readWriteLock.isWriteLocked()) {
            Thread.sleep(100);
		}
		if (file.exists()) {
			try {
				fileRd = new FileReader(file.getAbsoluteFile());
				buffRd = new BufferedReader(fileRd);
				String fileContent = buffRd.readLine();
				if (fileContent != null) {
					String content[] = fileContent.split(",");
					while (content != null) {
						if (productId == Long.parseLong(content[1])) {
							listOfReviewIDs.add(Long.parseLong(content[0]));
						}
						fileContent = buffRd.readLine();
						if (fileContent != null) {
							content = fileContent.split(",");
						} else {
							break;
						}
					}
				}
			} finally {
                buffRd.close();
                fileRd.close();
			}
		} else {
			logger.error("LookUp.txt file does not exist!");
		}
		return listOfReviewIDs;
	}

	@Override
	public List<Long> fetchAllReviewIDs(User user) throws InterruptedException, IOException {
		long userId = user.getId();
		File file = new File(lookUpFilePath);
		List<Long> listOfReviewIDs = new ArrayList<Long>();
		while(readWriteLock.isWriteLocked()) {
            Thread.sleep(100);
		}
		if (file.exists()) {
			try {
				fileRd = new FileReader(file.getAbsoluteFile());
				buffRd = new BufferedReader(fileRd);
				String fileContent = buffRd.readLine();
				if (fileContent != null) {
					String content[] = fileContent.split(",");
					while (content != null) {
						if (userId == Long.parseLong(content[2])) {
							listOfReviewIDs.add(Long.parseLong(content[0]));
						}
						fileContent = buffRd.readLine();
						if (fileContent != null) {
							content = fileContent.split(",");
						} else {
							break;
						}
					}
				}
			} finally {
                buffRd.close();
                fileRd.close();
			}
		} else {
			logger.error("LookUp.txt file does not exist!");
		}
		return listOfReviewIDs;
	}
	
	@Override
	public boolean delete(Review review) throws InterruptedException, IOException {
		long reviewId = review.getId();
		File lookUpFile = new File(lookUpFilePath);
		
		while(readWriteLock.isWriteLocked()) {
            Thread.sleep(100);
		}
		write.lock();
		
		String tempLookUpFilePath = FileUtil.getRootPath() + "tempReviewLookUp.txt";
		File tempLookUpFile = new File(tempLookUpFilePath);

        buffRd = new BufferedReader(new FileReader(lookUpFile));
        buffWr = new BufferedWriter(new FileWriter(tempLookUpFile));

        String lineToRemove = reviewId + ",";
        String currentLine;

        // write all entries into temp until reviewId is found
        while ((currentLine = buffRd.readLine()) != null) {
            if (currentLine.startsWith(lineToRemove)) {
                break;
            }
            buffWr.write(currentLine + System.getProperty("line.separator"));
        }

        // write all the remaining entries
        while ((currentLine = buffRd.readLine()) != null) {
            buffWr.write(currentLine + System.getProperty("line.separator"));
        }
        buffWr.close();
        buffRd.close();
        lookUpFile.delete();
        tempLookUpFile.renameTo(lookUpFile);

        write.unlock();

		return true;
	}
	
	@Override
	public boolean delete(Product product) throws InterruptedException, IOException {
		long productId = product.getId();
		File lookUpFile = new File(lookUpFilePath);

		while(readWriteLock.isWriteLocked()) {
            Thread.sleep(100);
		}
		write.lock();
		
		String tempLookUpFilePath = FileUtil.getRootPath() + "tempProductLookUp.txt";
		File tempLookUpFile = new File(tempLookUpFilePath);

        BufferedReader reader = new BufferedReader(new FileReader(lookUpFile));
        BufferedWriter writer = new BufferedWriter(new FileWriter(tempLookUpFile));

        String lineToRemove = "," + productId + ",";
        String currentLine;

        while ((currentLine = reader.readLine()) != null) {
            if (currentLine.contains(lineToRemove))
                continue;
            writer.write(currentLine + System.getProperty("line.separator"));
        }
        writer.close();
        reader.close();
        lookUpFile.delete();
        tempLookUpFile.renameTo(lookUpFile);
        write.unlock();
		return true;
	}
	
	@Override
	public boolean delete(User user) throws InterruptedException, IOException {
		long userId = user.getId();
		File lookUpFile = new File(lookUpFilePath);

		while(readWriteLock.isWriteLocked()) {
            Thread.sleep(100);
		}
		write.lock();
		
		String tempLookUpFilePath = FileUtil.getRootPath() + "tempUserLookUp.txt";
		File tempLookUpFile = new File(tempLookUpFilePath);

        BufferedReader reader = new BufferedReader(new FileReader(lookUpFile));
        BufferedWriter writer = new BufferedWriter(new FileWriter(tempLookUpFile));

        String lineToRemove = "," + userId;
        String currentLine;

        while ((currentLine = reader.readLine()) != null) {
            if (currentLine.endsWith(lineToRemove))
                continue;
            writer.write(currentLine + System.getProperty("line.separator"));
        }
        writer.close();
        reader.close();
        lookUpFile.delete();
        tempLookUpFile.renameTo(lookUpFile);

        write.unlock();
		return true;
	}
	
	private boolean writeToFile(String writeContent) throws InterruptedException, IOException {
		file = new File(lookUpFilePath);
		while(readWriteLock.isWriteLocked()) {
            Thread.sleep(100);
		}
		try {
			write.lock();
			fileWr = new FileWriter(file.getAbsoluteFile(), true);
			buffWr = new BufferedWriter(fileWr);
			buffWr.write(writeContent);
			write.unlock();
		} finally {
            buffWr.close();
            fileWr.close();
		}
		return true;
	}
}
