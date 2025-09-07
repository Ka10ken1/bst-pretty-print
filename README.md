# BST Pretty Print

## Description 
1. Implement [`com.epam.rd.autocode.bstprettyprint.PrintableTree`](src/main/java/com/epam/rd/autocode/bstprettyprint/PrintableTree.java) `getInstance` method.
1. It should return a `PrintableTree` instance.

PrintableTree is a simple Binary Search Tree with no balancing.\
It may contain int values that are added via `add` method.\
The main challenge is that it may be pretty printed via `prettyPrint` method.
It means that all the tree should be converted to a String representing its inner structure from smaller  values to greater ones.
Also, you must use pseudographic symbols to show node connections.

Example:
- elements: `123 11 200 1 100 150 2000`
- pretty printed tree:
```
      ┌1
   ┌11┤
   │  └100
123┤
   │   ┌150
   └200┤
       └2000
```

Another example (of not so balanced tree):
- elements: `931 39 196 385 388 207 185 955 957 542 904 498 394`
- pretty printed tree:
```
   ┌39┐
   │  │   ┌185
   │  └196┤
   │      │   ┌207
   │      └385┤
   │          └388┐
   │              │       ┌394
   │              │   ┌498┘
   │              └542┤
   │                  └904
931┤
   └955┐
       └957

```
