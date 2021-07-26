package hust.cs.javacourse.search.index.impl;

import hust.cs.javacourse.search.index.AbstractTerm;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * <pre>
 * Term是AbstractTerm对象的派生子类.
 *      Term对象表示文本文档里的一个单词.
 *      必须实现下面二个接口:
 *          Comparable：可比较大小（字典序）,为了加速检索过程，字典需要将单词进行排序.
 *          FileSerializable：可序列化到文件或从文件反序列化.
 *   </pre>
 */
public class Term extends AbstractTerm {
    /**
     * 缺省构造
     */
    public Term(){}
    /**
     * 有参构造函数
     * @param content term
     */
    public Term(String content){
        super(content);
    }

    /**
     * 判断二个Term内容是否相同
     * @param obj ：要比较的另外一个Term
     * @return 如果内容相等返回true，否则返回false
     */
    @Override
    public boolean equals(Object obj){
        if(this == obj) return true;
        if(!(obj instanceof AbstractTerm)||obj==null) return false;
        //先调用hashcode()进行对比，如果相同再进行equals的比较,提高效率
        //if(hashCode()!=obj.hashCode()) return false;
        //调用字符串equals方法
        return this.content.equals(((Term)obj).content);
    }

    /**
     * 返回Term的字符串表示
     * @return 字符串
     */
    @Override
    public String toString(){return content;}

    /**
     * 返回Term内容
     * @return Term内容
     */
    public String getContent(){return content;}

    /**
     * 设置Term内容
     * @param content：Term的内容
     */
    public void setContent(String content){this.content = content;}

    /**
     * 比较二个Term大小（按字典序）
     * @param o： 要比较的Term对象
     * @return ： 返回二个Term对象的字典序差值
     */
    @Override
    public int compareTo(AbstractTerm o) {
        //调用String类的compareTo方法
        return this.content.compareTo(o.getContent());
    }

    /**
     * 写到二进制文件
     * @param out :输出流对象
     */
    public void writeObject(ObjectOutputStream out) {
        //序列化写入
        try {
            out.writeObject(this.content);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * 从二进制文件读
     * @param in ：输入流对象
     */
    public void readObject(ObjectInputStream in){
        //序列化读入
        try {
            this.content = (String)in.readObject();
        }catch(IOException|ClassNotFoundException e){
            e.printStackTrace();
        }
    }
}
