package com.ecommerce.data;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.ecommerce.data.design.IDataAccess;
import com.ecommerce.representation.design.IModel;
import com.ecommerce.util.FileUtil;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.apache.log4j.Logger;

public class DataAccessFileSystemImpl<T extends IModel> implements IDataAccess<T> {

    private final Logger logger = Logger.getLogger(this.getClass().getSimpleName());
    private static final ObjectMapper mapper =  new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	private static ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
	private static ConcurrentHashMap<String, Boolean> map = new ConcurrentHashMap<String, Boolean>();
	private static File file = null;

	@Override
	public <T extends IModel> void create(T model) throws IOException {
		String filePath = getFilePath(model);
        writer.writeValue(new File(filePath), model);
	}

	@Override
	public <T extends IModel> T read(T model) throws IOException, InterruptedException {
		String filePath = getFilePath(model);
		file = new File(filePath);
        String mapEntry = getMapEntry(model);
        while(map.containsKey(mapEntry)) {
            Thread.sleep(20);
        }
        return (T) mapper.readValue(file, model.getClass());
	}

	@Override
	public <T extends IModel> void update(T model) throws IOException, InterruptedException {
		String filePath = getFilePath(model);
		file = new File(filePath);
        String mapEntry = getMapEntry(model);
        while(map.containsKey(mapEntry)) {
            Thread.sleep(20);
        }
        map.put(mapEntry, true);
        writer.writeValue(file, model);
        map.remove(mapEntry);
	}

	@Override
	public <T extends IModel> boolean delete(T model) throws InterruptedException {
		String filePath = getFilePath(model);
		file = new File(filePath);
		String mapEntry = getMapEntry(model);
        while(map.containsKey(mapEntry)) {
            Thread.sleep(20);
        }
		map.put(mapEntry, true);
		Boolean isDeleted = file.delete();
		map.remove(mapEntry);
		return isDeleted;
	}

	@Override
	public <T extends IModel> boolean isPresent(T model) {
		String filePath = getFilePath(model);
		file = new File(filePath);
		return file.exists();
	}

	@Override
	public <T extends IModel> String getFilePath(T model) {
        StringBuffer filePath = FileUtil.getRootPath();
        filePath.append(model.getAliasName()).append("/").append(model.getId()).append(".JSON");
        return filePath.toString();
	}

	@Override
	public <T extends IModel> List<T> getAll(T model) throws IOException, InterruptedException {
        List<T> list = new ArrayList<T>();
        StringBuffer filePath = FileUtil.getRootPath();
        filePath.append(model.getAliasName());
        File folder = new File(filePath.toString());
        File[] listOfFiles = folder.listFiles();
        if(listOfFiles != null) {
            long id;
            int pos;
            for (File file : listOfFiles) {
                String fileName = file.getName();
                pos = file.getName().lastIndexOf(".");
                fileName = fileName.substring(0, pos);
                id = Long.parseLong(fileName);
                model.setId(id);
                list.add(read(model));
            }
        }
        return list;
	}
	
	private <T extends IModel> String getMapEntry(T model) {
        String resourceType = model.getAliasName();
        long resourceId = model.getId();
        return (resourceType + resourceId);
	}
}
