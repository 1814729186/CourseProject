package hust.cs.javacourse.search.index.impl;

import hust.cs.javacourse.search.index.*;

import java.io.*;
import java.util.*;

/**
 * Index是AbstractIndex的具体实现类
 * AbstractIndex是内存中的倒排索引对象的抽象父类.
 *      一个倒排索引对象包含了一个文档集合的倒排索引.
 *      内存中的倒排索引结构为HashMap，key为Term对象，value为对应的PostingList对象.
 *      另外在AbstractIndex里还定义了从docId和docPath之间的映射关系.
 *      必须实现下面接口:
 *          FileSerializable：可序列化到文件或从文件反序列化.
 */
public class Index extends AbstractIndex {

    /**
     * 缺省构造（不使用），应使用IndexBuilder构造该类对象
     */
    public Index(){}

    /**
     * 返回索引的字符串表示
     *
     * @return 索引的字符串表示
     */
    @Override
    public String toString() {
        //显示两个map中的相关内容
        StringBuilder stringBuffer = new StringBuilder();
        //dictionary相关打印信息
        stringBuffer.append("dictionary:");
        for(Map.Entry<AbstractTerm,AbstractPostingList> entry : termToPostingListMapping.entrySet()) {
            stringBuffer.append(entry.getKey().toString()+" ");
        }
        //文档信息
        stringBuffer.append("\ndocId-----docPath mapping\n");
        //使用Map.Entry内部类获得map中的数据
        for(Map.Entry<Integer,String> entry : docIdToDocPathMapping.entrySet()){
            stringBuffer.append(entry.getKey() + "\t---->\t"+entry.getValue() + "\n");
        }
        stringBuffer.append("PostingList:\n");
        for(Map.Entry<AbstractTerm,AbstractPostingList> entry : termToPostingListMapping.entrySet()) {
            stringBuffer.append(entry.getKey().toString()+"\t---->\t"+entry.getValue().toString() + "\n");
        }
        return stringBuffer.toString();
    }

    /**
     * 添加文档到索引，更新索引内部的HashMap
     *
     * @param document ：文档的AbstractDocument子类型表示
     */
    @Override
    public void addDocument(AbstractDocument document) {
        Posting posting = null;
        List<Integer> positions = null;
        boolean flag = false;
        //将文档添加到map中
        docIdToDocPathMapping.put(document.getDocId(), document.getDocPath());
        //将termTuple添加到文档
        for(AbstractTermTuple termTuple : document.getTuples()){
            //检查termTuple是否已经在map中出现
            if(!termToPostingListMapping.containsKey(termTuple.term)){
                //如果没有出现
                posting = new Posting();
                posting.setDocId(document.getDocId());
                posting.setFreq(termTuple.freq);
                positions = new ArrayList<>();
                positions.add(termTuple.curPos);
                posting.setPositions(positions);
                termToPostingListMapping.put(termTuple.term, new PostingList());
                termToPostingListMapping.get(termTuple.term).add(posting);
            }else{
                flag = false;
                //包含term
                for(int i = 0;i < termToPostingListMapping.get(termTuple.term).size();i ++){
                    if(termToPostingListMapping.get(termTuple.term).get(i).getDocId() == document.getDocId()){
                        termToPostingListMapping.get(termTuple.term).get(i).getPositions().add(termTuple.curPos);
                        termToPostingListMapping.get(termTuple.term).get(i).setFreq(termToPostingListMapping.get(termTuple.term).get(i).getFreq()+1);
                        flag = true;
                    }
                }
                if(flag == false){
                    posting = new Posting();
                    posting.setDocId(document.getDocId());
                    posting.setFreq(termTuple.freq);
                    positions = new ArrayList<>();
                    positions.add(termTuple.curPos);
                    posting.setPositions(positions);
                    termToPostingListMapping.get(termTuple.term).add(posting);
                }

            }
        }
        optimize();
    }

    /**
     * <pre>
     * 从索引文件里加载已经构建好的索引.内部调用FileSerializable接口方法readObject即可
     * @param file ：索引文件
     * </pre>
     */
    @Override
    public void load(File file) {
        if(file == null)    return;
        try{
            readObject(new ObjectInputStream(new FileInputStream(file)));
        }catch(IOException|NullPointerException e){
            e.printStackTrace();
        }
    }

    /**
     * <pre>
     * 将在内存里构建好的索引写入到文件. 内部调用FileSerializable接口方法writeObject即可
     * @param file ：写入的目标索引文件
     * </pre>
     */
    @Override
    public void save(File file) {
        try {
            writeObject(new ObjectOutputStream(new FileOutputStream(file)));
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * 返回指定单词的PostingList
     *
     * @param term : 指定的单词
     * @return ：指定单词的PostingList;如果索引字典没有该单词，则返回null
     */
    @Override
    public AbstractPostingList search(AbstractTerm term) {
        return termToPostingListMapping.get(term);
    }

    /**
     * 返回索引的字典.字典为索引里所有单词的并集
     *
     * @return ：索引中Term列表
     */
    @Override
    public Set<AbstractTerm> getDictionary() {
        return termToPostingListMapping.keySet();
    }

    /**
     * <pre>
     * 对索引进行优化，包括：
     *      对索引里每个单词的PostingList按docId从小到大排序
     *      同时对每个Posting里的positions从小到大排序
     * 在内存中把索引构建完后执行该方法
     * </pre>
     */
    @Override
    public void optimize() {
        for(Map.Entry<AbstractTerm,AbstractPostingList> entry : termToPostingListMapping.entrySet()){
            // 对term根据position排序
            for(int i = 0;i < entry.getValue().size();i ++){
                entry.getValue().get(i).sort();
            }
            // 整体排序
            entry.getValue().sort();
        }
    }

    /**
     * 根据docId获得对应文档的完全路径名
     *
     * @param docId ：文档id
     * @return : 对应文档的完全路径名
     */
    @Override
    public String getDocName(int docId) {
        return docIdToDocPathMapping.get(docId);
    }

    /**
     * 写到二进制文件
     *
     * @param out :输出流对象
     */
    @Override
    public void writeObject(ObjectOutputStream out) {
        try{
            out.writeObject(docIdToDocPathMapping.size());
            out.writeObject(termToPostingListMapping.size());
            for(Map.Entry<Integer,String> entry : docIdToDocPathMapping.entrySet()){
                out.writeObject(entry.getKey());
                out.writeObject(entry.getValue());
            }
            for(Map.Entry<AbstractTerm,AbstractPostingList> entry : termToPostingListMapping.entrySet()){
                entry.getKey().writeObject(out);
                entry.getValue().writeObject(out);
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * 从二进制文件读
     *
     * @param in ：输入流对象
     */
    @Override
    public void readObject(ObjectInputStream in) {
        int docSize = 0;
        int termSize = 0;
        //int postSize = 0;
        try {
            docSize = (Integer) in.readObject();
            termSize = (Integer) in.readObject();
            for(int i = 0; i < docSize; i++) {
                Integer docId = (Integer) in.readObject();
                String docPath = (String) in.readObject();
                docIdToDocPathMapping.put(docId, docPath);
            }
            for (int i = 0; i < termSize; i++) {
                AbstractTerm term = new Term();
                AbstractPosting posting = new Posting();
                term.readObject(in);
                AbstractPostingList postingList = new PostingList();
                postingList.readObject(in);
                termToPostingListMapping.put(term, postingList);
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void writePlainText(File file){
        try {
            FileWriter writer = new FileWriter(file);
            writer.write(toString());
            writer.close();//写入后关闭文件
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
