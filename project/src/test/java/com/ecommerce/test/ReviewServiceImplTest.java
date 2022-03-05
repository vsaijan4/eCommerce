package com.ecommerce.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.log4j.Logger;
import org.junit.*;

import com.ecommerce.constants.StatusConstants;
import com.ecommerce.representation.Review;
import com.ecommerce.service.ReviewServiceImpl;

public class ReviewServiceImplTest {

    private final Logger logger = Logger.getLogger(this.getClass().getSimpleName());

	private Review createDummyObject() {
		Review review = new Review();
		review.setProductId(1);
		review.setUserId(1);
		review.setRating(5.0F);
		review.setTitle("Dummy Title");
		review.setDescription("Dummy Description");
		return review;
	}

	private Review editDummyObject() {
		Review review = new Review();
		review.setId(101);
		review.setProductId(1);
		review.setUserId(1);
		review.setRating(3.3F);
		return review;
	}

    //@Test
	public void testAdd() {
		ReviewServiceImpl classUnderTest = new ReviewServiceImpl();
		Review review = createDummyObject();

		try {
			assertEquals(review, classUnderTest.add(createDummyObject()));

            review.setProductId(2);
            assertNotEquals(review, classUnderTest.add(createDummyObject()));

		} catch (IOException | InterruptedException e) {
            logger.error("Exception occured: "+e.getClass().getSimpleName());
		}
	}
	
	@Test
	public void testModify() {
		ReviewServiceImpl classUnderTest = new ReviewServiceImpl();
		Review review = editDummyObject();
		
		/*
		assertEquals(StatusConstants.MODIFY_SUCCESS, classUnderTest.modify(review));

		review.setProductId(12);
		assertEquals(StatusConstants.PRODUCT, classUnderTest.modify(review));
		
		review.setProductId(2);
		assertEquals(StatusConstants.DIFF_PRODUCT, classUnderTest.modify(review));
		review.setProductId(1);
		
		review.setUserId(2);
		assertEquals(StatusConstants.DIFF_USER, classUnderTest.modify(review));
		review.setUserId(1);
		
		review.setProductId(1);
		assertEquals(StatusConstants.MODIFY_SUCCESS, classUnderTest.modify(review));
		*/
	}
	
	@Test
	public void testDelete() {
		ReviewServiceImpl classUnderTest = new ReviewServiceImpl();
		Review review = createDummyObject();


		review.setProductId(10);
		/*
		assertEquals(StatusConstants.PRODUCT, classUnderTest.delete(review));
		review.setProductId(1);
		
		review.setId(200);
		assertEquals(StatusConstants.REVIEW, classUnderTest.delete(review));
		review.setId(101);
		
		review.setUserId(10);
		assertEquals(StatusConstants.DIFF_USER, classUnderTest.delete(review));
		review.setUserId(1);
		
		review.setProductId(3);
		assertEquals(StatusConstants.DIFF_PRODUCT, classUnderTest.delete(review));
		review.setProductId(1);
		
		review.setUserId(2);
		assertEquals(StatusConstants.DIFF_USER, classUnderTest.delete(review));
		*/
	}
	
	//@Test
	public void testGet() {
		ReviewServiceImpl classUnderTest = new ReviewServiceImpl();
		Review review = createDummyObject();
		try {
            review.setProductId(12);
            assertNotEquals(review, classUnderTest.get(createDummyObject()));

            review.setProductId(1);
            assertEquals(review, classUnderTest.get(createDummyObject()));
        } catch (IOException | InterruptedException e) {
            logger.error("Exception occured: "+e.getClass().getSimpleName());
        }
	}

	@Before
	public void setUpDB() {
		String currUserHome = System.getenv("HOME") + "/ecommerce/";
		String rootPath = System.getenv("HOME") + "/ecommerce/data/";

		StringBuffer srcLoc = new StringBuffer(currUserHome + "tempData/tempReview/");
		StringBuffer tgtLoc = new StringBuffer(rootPath + "review/");
		copyAllFilesAcrossDirectory(srcLoc.toString(), tgtLoc.toString());
		
		srcLoc = new StringBuffer(currUserHome + "tempData/tempProduct/");
		tgtLoc = new StringBuffer(rootPath + "product/");
		copyAllFilesAcrossDirectory(srcLoc.toString(), tgtLoc.toString());
		
		srcLoc = new StringBuffer(currUserHome + "tempData/tempUser/");
		tgtLoc = new StringBuffer(rootPath + "user/");
		copyAllFilesAcrossDirectory(srcLoc.toString(), tgtLoc.toString());
		
		String maxIDFile = rootPath + "MaxID.txt";
		File file = new File(maxIDFile);
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(file);
			writer.print("1:5\n2:105\n3:1\n");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			writer.close();
		}

		String lookUpReviewProductFile = rootPath + "LookUp.txt";
		file = new File(lookUpReviewProductFile);
		writer = null;
		try {
			writer = new PrintWriter(file);
			writer.print("101,1,1\n102,2,1\n103,3,1\n104,4,1\n105,5,1\n");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			writer.close();
		}
	}

	@After
	public void tearDownDB() {
		String rootPath = System.getenv("HOME") + "/ecommerce/data/";
		
		deleteFilesInDirectory(rootPath + "product/");
		deleteFilesInDirectory(rootPath + "review/");
		deleteFilesInDirectory(rootPath + "user/");
		
		String maxIDFile = rootPath + "MaxID.txt";
		File file = new File(maxIDFile);
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(file);
			writer.print("1:0\n2:100\n3:0\n");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			writer.close();
		}

		String lookUpFile = rootPath + "LookUp.txt";
		file = new File(lookUpFile);
		writer = null;
		try {
			writer = new PrintWriter(file);
			writer.print("");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			writer.close();
		}
		
		String upvoteFile = rootPath + "Upvote.txt";
		file = new File(upvoteFile);
		writer = null;
		try {
			writer = new PrintWriter(file);
			writer.print("");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			writer.close();
		}
	}

	private static void deleteFilesInDirectory(String dirPath) {
		try (DirectoryStream<Path> dir = Files.newDirectoryStream(Paths.get(dirPath))) {
			for (Path file : dir) {
				Files.delete(file);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void copyAllFilesAcrossDirectory(String sourcePath, String destPath) {
		Path destDir = Paths.get(destPath);
		try (DirectoryStream<Path> dir = Files.newDirectoryStream(Paths.get(sourcePath))) {
			for (Path file : dir) {
				String name = file.getFileName().toString();
				Files.copy(file, destDir.resolve(name));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
