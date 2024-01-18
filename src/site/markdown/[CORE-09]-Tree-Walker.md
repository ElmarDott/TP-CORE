# TreeWalker

@**since**: 1.0 > @**api-version**: 2.0 > **Dependencies**: none

Files:

- **API Interface** org.europa.together.business.[TreeWalker](https://git.elmar-dott.com/scm/repo/TogetherPlatform/TP-CORE/code/sources/Releases/src/main/java/org/europa/together/business/TreeWalker.java)
- **Domain Object** org.europa.together.domain.[TreeNode](https://git.elmar-dott.com/scm/repo/TogetherPlatform/TP-CORE/code/sources/Releases/src/main/java/org/europa/together/domain/TreeNode.java)
- **Implementation** org.europa.together.application.[ListTree](https://git.elmar-dott.com/scm/repo/TogetherPlatform/TP-CORE/code/sources/Releases/src/main/java/org/europa/together/application/ListTree.java)

---

The TreeWalker is a simple implementation of a tree data structure. As usage is suggested to store and display directory hierarchies or equal puropses. The internal reference is a simple List of TreeNodes. This approach is not designed for large structures. Because of this limitation the performance will slow down as bigger the tree grows.

**basic theory**

![](https://github.com/ElmarDott/TP-CORE/wiki/images/Tree.jpg)

Dealing with trees you need to pay attention on some circumstances and definitions to get the expected result. Every tree have just one root node. Except the root node, every node need to have a parent. If a  node do not have a child node it called leaf. The example tree in the picture have the following metrics:

- countNodes = 11
- getLeafs = {4,6,8,10,11} = 5

If the tree do not have any connection between two nodes, the tree is not complete or valid. By applicate the method *prune()* the subtree(s) whiteout root node will be deleted. There some functionality to investigate the status of the tree:

- *isElementOfTree* - check if a node is part of a tree structure, it not validate the tree
- *isLeaf* - check if a node is a leaf or not
- isEmpty - check if a tree has elements (nodes)
- *prune* - remove a given element and all his child nodes from the tree
- *merge* - mere the original tree with another tree and all its elemnts
- *validateTree* - a valid tree has no "unconnected" elements

**Domain Object**

The domain object is called TreeNode witch is stored in the list.

| Name      | DataType             |
| --------- | -------------------- |
| UUID      | String (Primary Key) |
| nodeName  | String               |
| parent    | UUID                 |
| &gt;T&lt; value | Object               |

**Samples**

```java
//create the tree
@Autowired
private TreeWalker<Boolean> treeWalker;

// add root
treeWalker.addRoot(new TreeNode<Boolean>("Root Node"));

//P:0
TreeNode<Boolean> n_01 = new TreeNode<>("01");
n_01.setParent(treeWalker.getRoot().getUuid());
treeWalker.addNode(n_01);
TreeNode<Boolean> n_02 = new TreeNode<>("02");
n_02.setParent(treeWalker.getRoot().getUuid());
treeWalker.addNode(n_02);
TreeNode<Boolean> n_03 = new TreeNode<>("03");
n_03.setParent(treeWalker.getRoot().getUuid());
treeWalker.addNode(n_03);
//P:1
TreeNode<Boolean> n_04 = new TreeNode<>("04");
n_04.setParent(n_01.getUuid());
treeWalker.addNode(n_04);
TreeNode<Boolean> n_05 = new TreeNode<>("05");
n_05.setParent(n_01.getUuid());
treeWalker.addNode(n_05);
//P:2
TreeNode<Boolean> n_06 = new TreeNode<>("06");
n_06.setParent(n_02.getUuid());
treeWalker.addNode(n_06);
//P:3
TreeNode<Boolean> n_07 = new TreeNode<>("07");
n_07.setParent(n_03.getUuid());
treeWalker.addNode(n_07);
//P:5
TreeNode<Boolean> n_08 = new TreeNode<>("08");
n_08.setParent(n_05.getUuid());
treeWalker.addNode(n_08);
//P:7
TreeNode<Boolean> n_09 = new TreeNode<>("09");
n_09.setParent(n_07.getUuid());
treeWalker.addNode(n_09);
TreeNode<Boolean> n_10 = new TreeNode<>("10");
n_10.setParent(n_07.getUuid());
treeWalker.addNode(n_10);
//P:9
TreeNode<Boolean> n_11 = new TreeNode<>("11");
n_11.setParent(n_09.getUuid());
treeWalker.addNode(n_11);

int countNodes = treeWalker.countNodes()
List<TreeNode<Boolean>> leafs = treeWalker.getLeafs();

boolean lookup = treeWalker.isElementOfTree(new TreeNode<>("99"));

treeWalker.prune(treeWalker.getElementByName("7").get(0));

boolean correctness = treeWalker.validateTree(treeWalker.getTree());

//empty the datastructure
treeWalker.clear();
```
