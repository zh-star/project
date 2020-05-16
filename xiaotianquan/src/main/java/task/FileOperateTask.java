package task;

import dao.FileOperatorDAO;
import app.FileMeta;

import java.io.File;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: STAR
 * Date: 2020 -01
 * Time: 16:15
 */
public class FileOperateTask implements FileScanCallback {

    @Override
    public void execute(File dir) {
        if(dir.isDirectory()) {
            //本地文件
            File[] children = dir.listFiles();
            //包装为本地的自定义文件类集合
            List<FileMeta> localMetas = compose(children);
            // 数据库文件，jdbc查询实现
            //（根据路径查询路径下所有文件夹/文件）
            List<FileMeta> metas = FileOperatorDAO.query(dir.getPath());

            //数据库有，本地文件没有
            for (FileMeta meta : metas) {
                if(!localMetas.contains(meta)) {
                    FileOperatorDAO.delete(meta);//如果meta是文件夹，还要删除子文件/子文件夹
                }
            }

            // 本地有，数据库没有的文件
            for (FileMeta localMeta : localMetas) {
                // 需要FileMeta实现hashCode() 和 equals()方法
                if(!metas.contains(localMeta)) {
                    FileOperatorDAO.insert(localMeta);
                }
            }

        }

    }

    private List<FileMeta> compose(File[] children) {
        List<FileMeta> metas = new ArrayList<>();
        if(children != null) {
            for(File child : children) {
               // metas.add(new FileMeta(child.getName(),child.getPath(),
               //         child.length(),child.lastModified(),child.isDirectory());
                metas.add(new FileMeta(child));
            }
        }
        return metas;
    }

}
