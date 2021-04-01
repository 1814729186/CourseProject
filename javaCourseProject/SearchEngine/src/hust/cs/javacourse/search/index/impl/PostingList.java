package hust.cs.javacourse.search.index.impl;

import hust.cs.javacourse.search.index.AbstractPosting;
import hust.cs.javacourse.search.index.AbstractPostingList;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * PostingList是PostingList对象的派生实现子类。
 *      PostingList对象包含了一个单词的Posting列表.
 *      必须实现下面接口:
 *          FileSerializable：可序列化到文件或从文件反序列化.
 * </pre>
 */
public class PostingList extends AbstractPostingList {
//    /**
//     * Posting列表，Posting必须是AbstractPosting子类型对象
//     */
//    protected List<AbstractPosting> list = new ArrayList<AbstractPosting>();



    /**
     * 添加Posting,要求不能有内容重复的posting
     * @param posting：Posting对象
     */
    public void add(AbstractPosting posting){}

    /**
     * 获得PosingList的字符串表示
     * @return ： PosingList的字符串表示
     */
    @Override
    public String toString(){return null;}

    /**
     * 添加Posting列表,,要求不能有内容重复的posting
     * @param postings：Posting列表
     */
    public void add(List<AbstractPosting> postings){}

    /**
     * 返回指定下标位置的Posting
     * @param index ：下标
     * @return： 指定下标位置的Posting
     */
    public AbstractPosting get(int index){return null;}

    /**
     * 返回指定Posting对象的下标
     * @param posting：指定的Posting对象
     * @return ：如果找到返回对应下标；否则返回-1
     */
    public int indexOf(AbstractPosting posting){return 0;}

    /**
     * 返回指定文档id的Posting对象的下标
     * @param docId ：文档id
     * @return ：如果找到返回对应下标；否则返回-1
     */
    public int indexOf(int docId){return 0;}

    /**
     * 是否包含指定Posting对象
     * @param posting： 指定的Posting对象
     * @return : 如果包含返回true，否则返回false
     */
    public boolean contains(AbstractPosting posting){return false;}

    /**
     * 删除指定下标的Posting对象
     * @param index：指定的下标
     */
    public void remove(int index){}

    /**
     * 删除指定的Posting对象
     * @param posting ：定的Posting对象
     */
    public void remove(AbstractPosting posting){}

    /**
     * 返回PostingList的大小，即包含的Posting的个数
     * @return ：PostingList的大小
     */
    public int size(){return 0;}

    /**
     * 清除PostingList
     */
    public void clear(){}

    /**
     * PostingList是否为空
     * @return 为空返回true;否则返回false
     */
    public boolean isEmpty(){return false;}

    /**
     * 根据文档id的大小对PostingList进行从小到大的排序
     */
    public void sort(){}

    /**
     * 写到二进制文件
     * @param out :输出流对象
     */
    public void writeObject(ObjectOutputStream out){}

    /**
     * 从二进制文件读
     * @param in ：输入流对象
     */
    public void readObject(ObjectInputStream in){}

}
