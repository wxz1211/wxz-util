package com.github.wxz.common.test;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import com.github.wxz.common.thread.ThreadPoolManager;
import com.github.wxz.common.utils.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * @author xianzhi.wang
 * @date 2018/4/20 -10:45
 */
public class ExcelUtilTest {
    private static final int PAGE_SIZE = 20000;
    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelUtilTest.class);

    public static void main(String[] args) {

        ExecutorService executorService = ThreadPoolManager.getThreadPool(ExcelUtilTest.class.getSimpleName(), 2);

        String readerCsvFilePath = "D:\\Follower.csv";
        String writerCsvFilePath = "src/main/resource/gubaUserFollowingRecvFinal.csv";
        try {
            CsvReader csvReader = new CsvReader(new FileInputStream(new File(readerCsvFilePath)), Charset.forName("UTF-8"));
            CsvWriter csvWriter = new CsvWriter(writerCsvFilePath, ',', Charset.forName("UTF-8"));
            csvReader.readHeaders();
            //获取表头
            String[] head = csvReader.getHeaders();

            try {
                long readCount = 0;
                int times = 0;
                List<UserFollowing> userFollowingList = new ArrayList<>();
                while (csvReader.readRecord()) {
                    readCount++;
                    UserFollowing userFollowing = new UserFollowing();
                    long current = System.currentTimeMillis();
                    for (int i = 0; i < head.length; i++) {
                        if (StringUtils.equalsIgnoreCase("FollowingID", head[i])) {
                            userFollowing.setFollowingUID(csvReader.get(head[i]));
                        }
                        if (StringUtils.equalsIgnoreCase("FollowerID", head[i])) {
                            userFollowing.setUid(csvReader.get(head[i]));
                        }
                        if (StringUtils.equalsIgnoreCase("State", head[i])) {
                            if (StringUtils.equalsIgnoreCase("false", csvReader.get(head[i]))) {
                                userFollowing.setDelete(false);
                            } else {
                                userFollowing.setDelete(true);
                            }
                        }
                        if (StringUtils.equalsIgnoreCase("UpdateTime", head[i])) {
                            userFollowing.setOptDateTime(DateUtil.strToDate(csvReader.get(head[i]), DateUtil.yyyy_MM_dd_HH_mm_ss_SSS1));
                        }
                    }
                    userFollowing.setSrcType("guba");
                    userFollowing.setMongoUpdateTimestamp(current);
                    userFollowingList.add(userFollowing);
                    if (userFollowingList.size() == PAGE_SIZE) {
                        if (times == 0) {
                            String[] header = {"followingUID", "uid", "mongoUpdateTimestamp", "optDateTime", "delete", "srcType"};
                            csvWriter.writeRecord(header);
                        }
                        times++;
                        handle(userFollowingList, csvWriter);
                        LOGGER.info("times {} readCount {} ", times, readCount);
                        System.out.println("times " + times + " readCount " + readCount);
                        userFollowingList.clear();
                    }
                }
                csvReader.close();
                if (!userFollowingList.isEmpty()) {
                    handle(userFollowingList, csvWriter);
                }
                csvWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void handle(List<UserFollowing> userFollowingList, CsvWriter csvWriter) {
        userFollowingList.stream().forEach(uf -> {
            String[] contents = {
                    uf.getFollowingUID(),
                    uf.getUid(),
                    String.valueOf(uf.getMongoUpdateTimestamp())
                    , DateUtil.dateToStr(uf.getOptDateTime(), DateUtil.yyyy_MM_dd_HH_mm_ss_SSS1),
                    String.valueOf(uf.isDelete()),
                    uf.getSrcType()};
            try {
                csvWriter.writeRecord(contents);
            } catch (IOException e) {
                LOGGER.error(" {} ", e);
            }

        });
    }

}
