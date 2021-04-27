package com.ldongxu.util;

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.*;

/**
 * 敏感词匹配
 *
 * @author liudongxu06
 * @since 2019-06-21
 */
public class WordFilter {
    /**
     * 敏感词匹配规则
     */
    public enum MatchType {
        MIN_MATCH,//最小匹配规则，如：敏感词库["中国","中国人"]，语句："我是中国人"，匹配结果：我是[中国]人
        MAX_MATCH //最大匹配规则，如：敏感词库["中国","中国人"]，语句："我是中国人"，匹配结果：我是[中国人]
    }

    private static String ENCODING = "utf-8";
    /**
     * 敏感词字典库
     */
    private Map sensitiveWordMap = null;

    public WordFilter(Set<String> sensitiveWordSet) {
        init(sensitiveWordSet);
    }

    public WordFilter(InputStream inputStream, String charsetName) {
        init(readSensitiveWordFile(inputStream, charsetName));
    }

    public WordFilter(InputStream inputStream) {
        this(inputStream, null);
    }

    public synchronized void init(Set<String> sensitiveWordSet) {
        if (sensitiveWordSet == null || sensitiveWordSet.isEmpty()) {
            sensitiveWordMap = new HashMap();
            throw new IllegalArgumentException("初始化空的过滤词库");
        }
        initSensitiveWordMap(sensitiveWordSet);
    }

    /**
     * 判断是否包含敏感词
     *
     * @param txt       输入文本
     * @param matchType 匹配规则
     * @return 若包含返回true，否则返回false
     */
    public boolean checkContains(String txt, MatchType matchType) {
        boolean flag = false;
        for (int i = 0; i < txt.length(); i++) {
            int matchLength = checkSensitiveWord(txt, i, matchType);
            if (matchLength > 0) {
                flag = true;
            }
        }
        return flag;
    }

    public boolean checkContains(String txt) {
        return checkContains(txt, MatchType.MIN_MATCH);
    }

    /**
     * 获取文字中的敏感词
     *
     * @param txt       输入文本
     * @param matchType 匹配规则
     * @return
     */
    public Set<String> getSensitiveWord(String txt, MatchType matchType) {
        Set<String> sensitiveWords = new HashSet<>();
        for (int i = 0; i < txt.length(); i++) {
            int matchLength = checkSensitiveWord(txt, i, matchType);
            if (matchLength > 0) {//存在敏感词
                sensitiveWords.add(txt.substring(i, i + matchLength));
                i = i + matchLength - 1;
            }
        }
        return sensitiveWords;
    }

    public Set<String> getSensitiveWord(String txt) {
        return getSensitiveWord(txt, MatchType.MIN_MATCH);
    }

    private Set<String> readSensitiveWordFile(InputStream inputStream, String charsetName) {
        if (StringUtils.isBlank(charsetName)) charsetName = ENCODING;
        Set<String> set = null;
        InputStreamReader reader = null;
        try {
            reader = new InputStreamReader(inputStream, charsetName);
            set = new HashSet<>();
            BufferedReader bufferedReader = new BufferedReader(reader);
            String txt = null;
            while ((txt = bufferedReader.readLine()) != null) {
                set.add(txt);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return set;
    }

    /**
     * 初始化敏感词字典库，构建DFA算法模型
     *
     * @param sensitiveWordSet 敏感词库
     */
    private void initSensitiveWordMap(Set<String> sensitiveWordSet) {
        sensitiveWordMap = new HashMap(sensitiveWordSet.size());
        Iterator<String> iterator = sensitiveWordSet.iterator();
        while (iterator.hasNext()) {
            String sensitiveWord = iterator.next();
            Map nowMap = sensitiveWordMap;
            for (int i = 0; i < sensitiveWord.length(); i++) {
                char sensitiveChar = sensitiveWord.charAt(i);
                Object charMap = nowMap.get(sensitiveChar);
                if (charMap != null) {
                    nowMap = (Map) charMap;
                } else {
                    Map<String, String> newCharMap = new HashMap<>();
                    newCharMap.put("isEnd", "0");
                    nowMap.put(sensitiveChar, newCharMap);
                    nowMap = newCharMap;

                }
                if (i == sensitiveWord.length() - 1) {//最后一个字
                    nowMap.put("isEnd", "1");
                }
            }
        }
        System.out.println("过滤词库数据：" + sensitiveWordMap);
    }

    /**
     * 检查给定的文本中是否包含敏感词
     *
     * @param text       给定文本
     * @param beginIndex 检查开始位置
     * @param matchType  匹配规则
     * @return
     */
    private int checkSensitiveWord(String text, int beginIndex, MatchType matchType) {
        checkSensitiveWordMap();
        boolean flag = false;
        int matchLength = 0;
        StringBuilder sb = new StringBuilder();
        Map nowMap = sensitiveWordMap;
        for (int i = beginIndex; i < text.length(); i++) {
            char c = text.charAt(i);
            nowMap = (Map) nowMap.get(c);
            if (nowMap != null) {
                sb.append(c);
                matchLength++;
                if ("1".equals(nowMap.get("isEnd"))) {
                    flag = true;
                    if (MatchType.MIN_MATCH == matchType) {//最小规则，直接返回,最大规则还需继续查找
                        break;
                    }
                }
            } else {
                break;
            }
        }
        if (matchLength < 2 || !flag) {//长度必须大于等于2，为词
            matchLength = 0;
        } else {
//            System.out.println("过滤词："+sb.toString());
        }
        return matchLength;
    }

    private void checkSensitiveWordMap() {
        if (sensitiveWordMap == null) {
            throw new RuntimeException("ERROR: must init sensitiveWordMap");
        }
    }

}
