package com.company;

import java.util.ArrayList;
import java.util.NoSuchElementException;

public class MinHeap<E extends Comparable<E>> {

    private ArrayList<E> contents;
    private int size;
    // YOUR CODE HERE (no code should be needed here if not
    // implementing the more optimized version)

    /* Initializes an empty MinHeap. */
    public MinHeap() {
        contents = new ArrayList<>();
        contents.add(null);
    }

    /* Returns the element at index INDEX, and null if it is out of bounds. */
    private E getElement(int index) {
        if (index >= contents.size()) {
            return null;
        } else {
            return contents.get(index);
        }
    }

    /* Sets the element at index INDEX to ELEMENT. If the ArrayList is not big
       enough, add elements until it is the right size. */
    private void setElement(int index, E element) {
        while (index >= contents.size()) {
            contents.add(null);
        }
        contents.set(index, element);
    }

    /* Swaps the elements at the two indices. */
    private void swap(int index1, int index2) {
        E element1 = getElement(index1);
        E element2 = getElement(index2);
        setElement(index2, element1);
        setElement(index1, element2);
    }

    /* Prints out the underlying heap sideways. Use for debugging. */
    @Override
    public String toString() {
        return toStringHelper(1, "");
    }

    /* Recursive helper method for toString. */
    private String toStringHelper(int index, String soFar) {
        if (getElement(index) == null) {
            return "";
        } else {
            String toReturn = "";
            int rightChild = getRightOf(index);
            toReturn += toStringHelper(rightChild, "        " + soFar);
            if (getElement(rightChild) != null) {
                toReturn += soFar + "    /";
            }
            toReturn += "\n" + soFar + getElement(index) + "\n";
            int leftChild = getLeftOf(index);
            if (getElement(leftChild) != null) {
                toReturn += soFar + "    \\";
            }
            toReturn += toStringHelper(leftChild, "        " + soFar);
            return toReturn;
        }
    }

    /* Returns the index of the left child of the element at index INDEX. */
    private int getLeftOf(int index) {
        return (2 * index);
    }

    /* Returns the index of the right child of the element at index INDEX. */
    private int getRightOf(int index) {
        return ((2 * index) + 1);
    }

    /* Returns the index of the parent of the element at index INDEX. */
    private int getParentOf(int index) {
        return (index / 2);
    }

    /* Returns the index of the smaller element. At least one index has a
       non-null element. If the elements are equal, return either index. */
    private int min(int index1, int index2) {
        int result = contents.get(index1).compareTo(contents.get(index2));
        if (result == 0) {
            return index1;
        } else if (result > 0) {
            return index2;
        } else {
            return index1;
        }
    }

    /* Returns but does not remove the smallest element in the MinHeap. */
    public E findMin() {
        int minIndex = 1;
        for (int i = 1; i < contents.size(); i++) {
            if (contents.get(i).compareTo(contents.get(minIndex)) < 0) {
                minIndex = i;
            }
        }
        return contents.get(minIndex);
    }

    /* Bubbles up the element currently at index INDEX. */
    private void bubbleUp(int index) {
        while (index != 1 && contents.get(index).compareTo(contents.get(getParentOf(index))) < 0) {
            swap(index, getParentOf(index));
            index = getParentOf(index);
        }
    }

    /* Bubbles down the element currently at index INDEX. */
    private void bubbleDown(int index) {
        while (getLeftOf(index) < contents.size() && getRightOf(index) < contents.size()
                && ((contents.get(index).compareTo(contents.get(getLeftOf(index))) > 0)
                || (contents.get(index).compareTo(contents.get(getRightOf(index))) > 0))) {
            swap(index, min(getLeftOf(index), getRightOf(index)));
            index = min(getLeftOf(index), getRightOf(index));
        }
    }

    /* Returns the number of elements in the MinHeap. */
    public int size() {
        return this.size;
    }

    /* Inserts ELEMENT into the MinHeap. If ELEMENT is already in the MinHeap,
       throw an IllegalArgumentException.*/
    public void insert(E element) {
        if (contents.contains(element)) {
            throw new IllegalArgumentException();
        }
        contents.add(element);
        int currIndex = contents.size() - 1;
        bubbleUp(currIndex);
        this.size++;
    }

    /* Returns and removes the smallest element in the MinHeap. */
    public E removeMin() {
        int minIndex = 1;
        for (int i = 1; i < contents.size(); i++) {
            if (contents.get(i).compareTo(contents.get(minIndex)) < 0) {
                minIndex = i;
            }
        }
        E result = contents.get(minIndex);
        swap(minIndex, contents.size() - 1);
        contents.remove(contents.size() - 1);
        bubbleDown(1);
        this.size--;
        return result;
    }

    /* Replaces and updates the position of ELEMENT inside the MinHeap, which
       may have been mutated since the initial insert. If a copy of ELEMENT does
       not exist in the MinHeap, throw a NoSuchElementException. Item equality
       should be checked using .equals(), not ==. */
    public void update(E element) {
        if (contents.contains(element)) {
            for (int i = 1; i < contents.size(); i++) {
                if (contents.get(i).equals(element)) {
                    setElement(i, element);
                    bubbleUp(i);
                    bubbleDown(i);
                }
            }
        } else {
            throw new NoSuchElementException();
        }
    }

    /* Returns true if ELEMENT is contained in the MinHeap. Item equality should
       be checked using .equals(), not ==. */
    public boolean contains(E element) {
        for (int i = 1; i < contents.size(); i++) {
            if (contents.get(i).equals(element)) {
                return true;
            }
        }
        return false;
    }
}

