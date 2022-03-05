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

import com.ecommerce.data.design.IUpvoteDataAccess;
import com.ecommerce.representation.Review;
import com.ecommerce.representation.User;
import com.ecommerce.util.FileUtil;

public class UpvoteFileSystemImpl implements IUpvoteDataAccess {

	private final Logger logger = Logger.getLogger(this.getClass().getSimpleName());
	
	private static final String upvoteFilePath = FileUtil.getRootPath() + "Upvote.txt";
	private static File file = null;
	private static FileReader fileRd = null;
	private static BufferedReader buffRd = null;
	private static FileWriter fileWr = null;
	private static BufferedWriter buffWr = null;
	private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
	private final Lock write = readWriteLock.writeLock();
	
	@Override
	public boolean create(Review review, int vote) {
		long reviewId = review.getId();
		long userId = review.getUserId();
		
		return writeToFile(reviewId + "," + userId + "," + vote + "\n");
	}

	@Override
	public int read(Review review) {
		long reviewId = review.getId();
		long userId = review.getUserId();

		while(readWriteLock.isWriteLocked()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		file = new File(upvoteFilePath);
		if (file.exists()) {
			try {
				fileRd = new FileReader(file.getAbsoluteFile());
				buffRd = new BufferedReader(fileRd);
				String fileContent = buffRd.readLine();
				if (fileContent != null) {
					long currReviewId, currUserId;
					String content[] = fileContent.split(",");
					while (content != null) {
						currReviewId = Long.parseLong(content[0]);
						currUserId = Long.parseLong(content[1]);
						if (currReviewId == reviewId && currUserId == userId) {
							return Integer.parseInt(content[2]);
						}
						fileContent = buffRd.readLine();
						if (fileContent != null) {
							content = fileContent.split(",");
						} else {
							break;
						}
					}
				}
			} catch (Exception exp) {
				exp.printStackTrace();
			} finally {
				try {
					buffRd.close();
					fileRd.close();
				} catch (IOException ioExp) {
					ioExp.printStackTrace();
				}
			}
		} else {
			logger.error("Upvote.txt file does not exist!");
		}
		return -1;
	}
	
	@Override
	public boolean update(Review review, long vote) {
		long reviewId = review.getId();
		long userId = review.getUserId();
		while(readWriteLock.isWriteLocked()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		write.lock();
		file = new File(upvoteFilePath);
	 	String tempUpvoteFilePath = FileUtil.getRootPath() + "tempUpdateVote.txt";
		File tempFile = new File(tempUpvoteFilePath);
		try {
			buffRd = new BufferedReader(new FileReader(file));
			buffWr = new BufferedWriter(new FileWriter(tempFile));

			String lineToRemove = reviewId + "," + userId + ",";
			String currentLine;

			while ((currentLine = buffRd.readLine()) != null) {
				String trimmedLine = currentLine.trim();
				if (trimmedLine.startsWith(lineToRemove)) {
					currentLine = reviewId + "," + userId + "," + vote;
				}
				buffWr.write(currentLine + System.getProperty("line.separator"));
			}
			buffWr.close();
			buffRd.close();
			file.delete();
			tempFile.renameTo(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		write.unlock();
		return true;
	}
	
	@Override
	public boolean delete(Review review) {
		long reviewId = review.getId();
		while(readWriteLock.isWriteLocked()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		write.lock();
		file = new File(upvoteFilePath);
	 	String tempUpvoteFilePath = FileUtil.getRootPath() + "tempReviewVote.txt";
		File tempFile = new File(tempUpvoteFilePath);
		try {
			buffRd = new BufferedReader(new FileReader(file));
			buffWr = new BufferedWriter(new FileWriter(tempFile));

			String lineToRemove = reviewId + ",";
			String currentLine;

			while ((currentLine = buffRd.readLine()) != null) {
				if (currentLine.startsWith(lineToRemove)) {
					continue;
				}
				buffWr.write(currentLine + System.getProperty("line.separator"));
			}
			buffWr.close();
			buffRd.close();
			file.delete();
			tempFile.renameTo(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		write.unlock();
		return true;
	}
	
	@Override
	public boolean delete(User user) {
		long userId = user.getId();
		while(readWriteLock.isWriteLocked()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		write.lock();
		file = new File(upvoteFilePath);
	 	String tempUpvoteFilePath = FileUtil.getRootPath() + "tempReviewVote.txt";
		File tempFile = new File(tempUpvoteFilePath);
		try {
			buffRd = new BufferedReader(new FileReader(file));
			buffWr = new BufferedWriter(new FileWriter(tempFile));

			String lineToRemove = "," + userId + ",";
			String currentLine;

			while ((currentLine = buffRd.readLine()) != null) {
				if (currentLine.contains(lineToRemove)) {
					continue;
				}
				buffWr.write(currentLine + System.getProperty("line.separator"));
			}
			buffWr.close();
			buffRd.close();
			file.delete();
			tempFile.renameTo(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		write.unlock();
		return true;
	}
	
	private boolean writeToFile(String writeContent) {
		while(readWriteLock.isWriteLocked()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		write.lock();
		file = new File(upvoteFilePath);
		try {
			fileWr = new FileWriter(file.getAbsoluteFile(), true);
			buffWr = new BufferedWriter(fileWr);
			buffWr.write(writeContent);
			write.unlock();
			return true;
		} catch (Exception exp) {
			exp.printStackTrace();
		} finally {
			try {
				buffWr.close();
				fileWr.close();
			} catch (IOException ioExp) {
				ioExp.printStackTrace();
			}
		}
		return false;
	}
}
