package hust.cs.javacourse.search.parse.impl;

import hust.cs.javacourse.search.index.AbstractTermTuple;
import hust.cs.javacourse.search.index.impl.Term;
import hust.cs.javacourse.search.index.impl.TermTuple;
import hust.cs.javacourse.search.parse.AbstractTermTupleScanner;
import hust.cs.javacourse.search.util.Config;
import hust.cs.javacourse.search.util.StringSplitter;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Queue;

/**
 * <pre>
 *     TermTupleScanner是AbstractTermTupleScanner的派生子类
 *     一个具体的TermTupleScanner对象就是一个AbstractTermTupleStream流对象，它利用java.io.BufferedReader去读取文本文件得到一个个三元组TermTuple.
 *
 *     其具体子类需要重新实现next方法获得文本文件里的三元组
 * </pre>
 */
public class TermTupleScanner extends AbstractTermTupleScanner {
    /**
     * 记录当前位置
     */
    int pos = 0;
    /**
     * 队列，维护和处理读入的字符串信息
     */
    Queue<AbstractTermTuple> buffer = new LinkedList<>();
    /**
     * 构造函数
     * @param input BufferedReader对象
     */
    public TermTupleScanner(BufferedReader input){
        super(input);   //调用父类构造函数
    }
    /**
     * 获得下一个三元组
     * @return : 下一个三元组；如果到了流的末尾，返回null
     * @throws IOException : 可能抛出IO异常
     */
    public AbstractTermTuple next() throws IOException {
        if (buffer.isEmpty()) {
            String string = input.readLine();
            if (string == null) {
                return null;
            }
            while (string.trim().length() == 0) {
                string = input.readLine();
                if (string == null) {
                    return null;
                }
            }
            StringSplitter splitter = new StringSplitter();
            splitter.setSplitRegex(Config.STRING_SPLITTER_REGEX);
            for (String word : splitter.splitByRegex(string)) {
                TermTuple tt = new TermTuple();
                tt.curPos = pos;
                if (Config.IGNORE_CASE) {
                    tt.term = new Term(word.toLowerCase());
                } else {
                    tt.term = new Term(word);
                }
                buffer.add(tt);
                pos ++;
            }
        }
        return buffer.poll();
    }
}
