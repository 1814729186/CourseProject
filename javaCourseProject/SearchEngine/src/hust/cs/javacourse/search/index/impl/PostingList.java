package hust.cs.javacourse.search.index.impl;

import hust.cs.javacourse.search.index.AbstractPosting;
import hust.cs.javacourse.search.index.AbstractPostingList;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
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

    /**
     * 添加Posting,要求不能有内容重复的posting
     * @param posting：Posting对象
     */
    public void add(AbstractPosting posting){
        if(contains(posting)) return;
        list.add(posting);
    }

    /**
     * 获得PosingList的字符串表示
     * @return ： PosingList的字符串表示
     */
    @Override
    public String toString(){
        //调用列表中每个对象的toString()方法转换成字符串再进行拼接
        StringBuilder builder = new StringBuilder();
        boolean flag = false;
        for (AbstractPosting posting : list) {
            if (flag) {
                builder.append("->");
            }
            flag = true;
            builder.append(posting.toString());
        }
        return builder.toString();
    }

    /**
     * 添加Posting列表,,要求不能有内容重复的posting
     * @param postings：Posting列表
     */
    public void add(List<AbstractPosting> postings){
        // 调用实例方法add依次将posting添加到列表中，方法中有是否相同的判断
        for(AbstractPosting posting : postings){
            this.add(posting);
        }
    }

    /**
     * 返回指定下标位置的Posting
     * @param index ：下标
     * @return： 指定下标位置的Posting
     */
    public AbstractPosting get(int index){
        return list.get(index);
    }

    /**
     * 返回指定Posting对象的下标
     * @param posting：指定的Posting对象
     * @return ：如果找到返回对应下标；否则返回-1
     */
    public int indexOf(AbstractPosting posting){
        return list.indexOf(posting);
    }

    /**
     * 返回指定文档id的Posting对象的下标
     * @param docId ：文档id
     * @return ：如果找到返回对应下标；否则返回-1
     */
    public int indexOf(int docId){
        // 遍历查找
        for(int i = 0;i < list.size();i ++){
            if(list.get(i).getDocId() == docId)
                return i;
        }
        return -1;
    }

    /**
     * 是否包含指定Posting对象
     * @param posting： 指定的Posting对象
     * @return : 如果包含返回true，否则返回false
     */
    public boolean contains(AbstractPosting posting){
        return list.contains(posting);
    }

    /**
     * 删除指定下标的Posting对象
     * @param index：指定的下标
     */
    public void remove(int index){
        list.remove(index);
    }

    /**
     * 删除指定的Posting对象
     * @param posting ：定的Posting对象
     */
    public void remove(AbstractPosting posting){
        list.remove(posting);
    }

    /**
     * 返回PostingList的大小，即包含的Posting的个数
     * @return ：PostingList的大小
     */
    public int size(){return list.size();}

    /**
     * 清除PostingList
     */
    public void clear(){
        list.clear();
    }

    /**
     * PostingList是否为空
     * @return 为空返回true;否则返回false
     */
    public boolean isEmpty(){
        return list.isEmpty();
    }

    /**
     * 根据文档id的大小对PostingList进行从小到大的排序
     */
    public void sort(){
        // 调用posting的equals方法，根据返回值进行排序
        Collections.sort(this.list);
    }

    /**
     * 写到二进制文件
     * @param out :输出流对象
     */
    public void writeObject(ObjectOutputStream out){
        /*try {
            for(int i = 0;i < list.size();i++){
                out.writeObject(list.get(i));
            }
        }catch(IOException e){
            e.printStackTrace();
        }*/
        try {
            out.writeObject(list.size());
            for (AbstractPosting posting : list) {
                posting.writeObject(out);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从二进制文件读
     * @param in ：输入流对象
     */
    public void readObject(ObjectInputStream in){
       /* try{
            while(true){
                add((Posting)in.readObject());
            }
        }catch(EOFException e){
            return;
        }catch(IOException | ClassNotFoundException e){
            e.printStackTrace();
        }*/
        try {
            int size = (Integer) in.readObject();
            for (int i = 0; i < size; i ++) {
                AbstractPosting posting = new Posting();
                posting.readObject(in);
                list.add(posting);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
