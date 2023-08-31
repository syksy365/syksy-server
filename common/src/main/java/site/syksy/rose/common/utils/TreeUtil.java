package site.syksy.rose.common.utils;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Raspberry
 * @author https://chenjianhui.site/2019-08-29-essay/
 */
public class TreeUtil {

    /**
     * 获取上一个节点
     *
     * @param root
     * @param loadChildrenNodes
     * @param loadNextKey
     * @param key
     * @param <T>
     * @param <R>
     * @return
     */
    public static <T, R> T findPreviousNode(List<T> root, Function<T, List<T>> loadChildrenNodes, Function<T, R> loadNextKey, R key) {
        Stack<T> stack = new Stack<>();
        root.forEach(stack::push);
        while (!stack.isEmpty()) {
            T o = stack.pop();
            if (key.equals(loadNextKey.apply(o))) {
                return o;
            }
            List<T> children = loadChildrenNodes.apply(o);
            if (children != null && children.size() > 0) {
                children.forEach(stack::push);
            }
        }
        return null;
    }

    /**
     * 根据 parentKey 获取子节点
     *
     * @param root
     * @param loadChildrenNodes
     * @param loadKey
     * @param parentKey
     * @param <T>
     * @param <R>
     * @return
     */
    public static <T, R> List<T> getChildren(List<T> root, Function<T, List<T>> loadChildrenNodes, Function<T, R> loadKey, R parentKey) {
        if(parentKey == null){
            return root;
        }
        Stack<T> stack = new Stack<>();
        root.forEach(stack::push);
        while (!stack.isEmpty()) {
            T o = stack.pop();
            List<T> children = loadChildrenNodes.apply(o);
            if (loadKey.apply(o).equals(parentKey)) {
                return children;
            }
            if (children != null && children.size() > 0) {
                children.forEach(stack::push);
            }
        }
        return new ArrayList<>();
    }

    /**
     * 获取所有子节点ID
     *
     * @param root
     * @param loadChildrenNodes
     * @param loadKey
     * @param parentKey
     * @param <T>
     * @param <R>
     * @return
     */
    public static <T, R> List<R> getChildrenIds(List<T> root, Function<T, List<T>> loadChildrenNodes, Function<T, R> loadKey, R parentKey) {
        List<T> c = getChildren(root, loadChildrenNodes, loadKey, parentKey);
        List<R> ids = new ArrayList<>();
        Stack<T> stack = new Stack<>();
        c.forEach(stack::push);
        while (!stack.isEmpty()) {
            T o = stack.pop();
            ids.add(loadKey.apply(o));
            List<T> children = loadChildrenNodes.apply(o);
            if (children != null && children.size() > 0) {
                children.forEach(stack::push);
            }
        }
        return ids;
    }


    /**
     * 获取同层级下最后一个节点
     *
     * @param root
     * @param loadChildrenNodes
     * @param loadKey
     * @param loadNextKey
     * @param parentKey
     * @param <T>
     * @param <R>
     * @return
     */
    public static <T, R> T findLastNode(List<T> root, Function<T, List<T>> loadChildrenNodes, Function<T, R> loadKey, Function<T, R> loadNextKey, R parentKey) {
        List<T> children = getChildren(root, loadChildrenNodes, loadKey, parentKey);
        for (T t : children) {
            if (loadNextKey.apply(t) == null || "".equals(loadNextKey.apply(t))) {
                return t;
            }
        }
        return null;
    }

    /**
     * 获取从根节点到本节点的路径
     *
     * @param root
     * @param loadChildrenNodes
     * @param loadKey
     * @param key
     * @param <T>
     * @param <R>
     * @return
     */
    public static <T, R> List<R> findLinkIds(List<T> root, Function<T, List<T>> loadChildrenNodes, Function<T, R> loadKey, Function<T, R> loadParentKey, R key) {
        List<R> link = new ArrayList<>();
        Stack<T> stack = new Stack<>();
        root.forEach(stack::push);
        while (!stack.isEmpty()) {
            T o = stack.pop();
            R oKey = loadKey.apply(o);
            R oParentKey = loadParentKey.apply(o);
            if (oParentKey == null) {
                link.clear();
            }
            while (oParentKey != null && !oParentKey.equals(link.get(link.size() - 1))) {
                link.remove(link.size() - 1);
            }
            link.add(oKey);
            if (oKey.equals(key)) {
                return link;
            }
            List<T> children = loadChildrenNodes.apply(o);
            if (children != null && children.size() > 0) {
                children.forEach(stack::push);
            } else {
                link.remove(oKey);
            }
        }
        return link;
    }

    /**
     * 获取从根节点到本节点的路径和值
     *
     * @param root
     * @param loadChildrenNodes
     * @param loadKey
     * @param loadParentKey
     * @param loadValue
     * @param key
     * @param keys
     * @param map
     * @param <T>
     * @param <R>
     */
    public static <T, R> void findLink(List<T> root, Function<T, List<T>> loadChildrenNodes, Function<T, R> loadKey, Function<T, R> loadParentKey, Function<T, R> loadValue, R key, List<R> keys, Map<R, R> map) {
        ArrayList<T> bl = new ArrayList<>();
        Stack<T> stack = new Stack<>();
        root.forEach(stack::push);
        while (!stack.isEmpty()) {
            T o = stack.pop();
            bl.add(o);

            R oKey = loadKey.apply(o);
            R oValue = loadValue.apply(o);
            R oParentKey = loadParentKey.apply(o);
            while (oParentKey != null && !oParentKey.equals(keys.get(keys.size() - 1))) {
                R r = keys.remove(keys.size() - 1);
                map.remove(r);
            }
            keys.add(oKey);
            map.put(oKey, oValue);

            if (oKey.equals(key)) {
                return;
            }
            List<T> children = loadChildrenNodes.apply(o);
            if (children != null && children.size() > 0) {
                children.forEach(stack::push);
            } else {
                keys.remove(oKey);
                map.remove(oKey);
            }
        }
    }


    /**
     * 深度遍历树结构
     *
     * @param root              节点树结构
     * @param loadChildrenNodes 加载树的子节点列表函数 接收一个节点 返回节点的子结构
     * @param behavior          遍历到的节点行为
     * @param <T>               树节点对象
     */
    public static <T> void traversing(List<T> root, Function<T, List<T>> loadChildrenNodes, Consumer<T> behavior) {
        Stack<T> stack = new Stack<>();
        root.forEach(stack::push);
        while (!stack.isEmpty()) {
            T o = stack.pop();
            behavior.accept(o);
            List<T> children = loadChildrenNodes.apply(o);
            if (children != null && children.size() > 0) {
                children.forEach(stack::push);
            }
        }
    }

    /**
     * 删除节点
     *
     * @param root
     * @param loadChildrenNodes
     * @param isRemove
     * @param <T>
     */
    public static <T> void remove(List<T> root, Function<T, List<T>> loadChildrenNodes, Function<T, Boolean> isRemove) {
        Iterator<T> iterator = root.iterator();
        while (iterator.hasNext()) {
            T o = iterator.next();
            if (isRemove.apply(o)) {
                iterator.remove();
            } else {
                List<T> children = loadChildrenNodes.apply(o);
                if (children != null && children.size() > 0) {
                    remove(children, loadChildrenNodes, isRemove);
                }
            }
        }
    }


    /**
     * 平铺树结构
     *
     * @param root              节点树结构
     * @param loadChildrenNodes 加载树的子节点列表函数 接收一个节点 返回节点的子结构
     * @param <T>               树节点对象
     * @return 平铺结构
     */
    public static <T> List<T> tileTree(List<T> root, Function<T, List<T>> loadChildrenNodes) {
        List<T> list = new ArrayList<>();
        traversing(root, loadChildrenNodes, list::add);
        return list;
    }

    /**
     * 聚合树结构
     *
     * @param list          节点列表结构
     * @param loadKey       节点唯一key读取 接收一个节点 返回节点的唯一key
     * @param loadParentKey 节点父节点key读取 接收一个节点 返回节点的父节点key
     * @param write         节点子项写入函数 接收待写入节点与节点子项 负责将子节点写入
     * @param <T>           节点对象
     * @param <R>           节点唯一key对象
     * @return 树结构
     */
    public static <T, R> List<T> polymerizationTree(List<T> list, Function<T, R> loadKey, Function<T, R> loadParentKey, Function<T, R> loadNextKey, BiConsumer<T, List<T>> write) {
        List<T> root = list.stream().filter(o -> loadParentKey.apply(o) == null).collect(Collectors.toList());
        root = sort(root, loadKey, loadNextKey);
        Stack<T> stack = new Stack<>();
        root.forEach(stack::push);
        while (!stack.isEmpty()) {
            T o = stack.pop();
            R key = loadKey.apply(o);
            List<T> children = list.stream()
                    .filter(k -> key.equals(loadParentKey.apply(k)))
                    .collect(Collectors.toList());
            if (children.size() > 0) {
                children = sort(children, loadKey, loadNextKey);
                children.forEach(stack::push);
            }
            write.accept(o, children);
        }
        return root;
    }

    /**
     * 聚合树结构
     *
     * @param list          节点列表结构
     * @param loadKey       节点唯一key读取 接收一个节点 返回节点的唯一key
     * @param loadParentKey 节点父节点key读取 接收一个节点 返回节点的父节点key
     * @param write         节点子项写入函数 接收待写入节点与节点子项 负责将子节点写入
     * @param isJoin        判断是否加入到树中
     * @param <T>           节点对象
     * @param <R>           节点唯一key对象
     * @return
     */
    public static <T, R> List<T> polymerizationTree(List<T> list, Function<T, R> loadKey, Function<T, R> loadParentKey, Function<T, R> loadNextKey, BiConsumer<T, List<T>> write, Function<T, Boolean> isJoin) {
        List<T> root = list.stream().filter(o -> loadParentKey.apply(o) == null).collect(Collectors.toList());
        root = sort(root, loadKey, loadNextKey);
        Stack<T> stack = new Stack<>();
        root.forEach(stack::push);
        while (!stack.isEmpty()) {
            T o = stack.pop();
            R key = loadKey.apply(o);

            List<T> children = list.stream()
                    .filter(k -> key.equals(loadParentKey.apply(k)))
                    .collect(Collectors.toList());

            /**List<T> children = list.stream()
             .filter(k ->
             isJoin.apply(k) && key.equals(loadParentKey.apply(k))
             )
             .collect(Collectors.toList());**/
            if (children.size() > 0) {
                children = sort(children, loadKey, loadNextKey);
                children = children.stream()
                        .filter(k -> isJoin.apply(k))
                        .collect(Collectors.toList());
                children.forEach(stack::push);
            }
            write.accept(o, children);
        }
        return root;
    }

    /**
     * 根据单链同级排序
     *
     * @param nodeList
     * @param loadKey
     * @param loadNextKey
     * @param <T>
     * @param <R>
     * @return
     */
    public static <T, R> List<T> sort(List<T> nodeList, Function<T, R> loadKey, Function<T, R> loadNextKey) {
        List<T> lastNodeArray = nodeList.stream().filter(i -> loadNextKey.apply(i) == null || "".equals(loadNextKey.apply(i))).collect(Collectors.toList());
        if(lastNodeArray.size() != 1){
            return nodeList;
        }
        T lastNode = nodeList.stream().filter(i -> loadNextKey.apply(i) == null || "".equals(loadNextKey.apply(i))).collect(Collectors.toList()).get(0);
        if (nodeList.size() == 1) {
            return nodeList;
        }
        List<T> newNode = new ArrayList<>(nodeList.size());
        nodeList.remove(lastNode);
        newNode.add(lastNode);
        T p = lastNode;
        while (nodeList.size() > 0) {
            for (int i = 0; i < nodeList.size(); i++) {
                T node = nodeList.get(i);
                if (loadKey.apply(p).equals(loadNextKey.apply(node))) {
                    p = node;
                    newNode.add(0, node);
                    nodeList.remove(node);
                }
            }
        }
        return newNode;
    }

}
