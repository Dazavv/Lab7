package me.dasha.lab7.managers;

import me.dasha.lab7.collectionClasses.Coordinates;
import me.dasha.lab7.collectionClasses.SpaceMarine;
import me.dasha.lab7.dataBase.DataBaseHandler;
import me.dasha.lab7.utility.User;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Logger;


/**
 * this class interacts with the collection
 */
public class CollectionManager {

    private static Stack<SpaceMarine> stack = new Stack<>();

    private static LocalDateTime creationDate;
    private static final Logger collectionManagerLogger = Logger.getLogger(CollectionManager.class.getName());
    private static final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    public CollectionManager() {
        loadCollectionFromBd();
    }
    public static void loadCollectionFromBd(){
        try {
            creationDate = LocalDateTime.now();
            stack.addAll(DataBaseHandler.loadCollection());
        } catch (NullPointerException e) {
            collectionManagerLogger.info("Коллекция пустая");
        }
    }

    /**
     * check the existence of the stack
     */
    public static void checkStack() {
        if (stack == null) {
            stack = new Stack<>();
            creationDate = LocalDateTime.now();
        }
    }
    public static boolean checkUsersId(int id, String username){
        String el = null;
        for (SpaceMarine spaceMarine : stack) {
            if (spaceMarine.getId().equals(id)) {
                el = spaceMarine.getOwner();
                break;
            }
        }
        return username.equals(el);
    }
    public static boolean checkUsersAchievements(String ach, String username) {
        String el = null;
        for (SpaceMarine spaceMarine : stack) {
            if (spaceMarine.getAchievements() != null && spaceMarine.getAchievements().equals(ach)) {
                el = spaceMarine.getOwner();
                break;
            }
        }
        return username.equals(el);
    }
    public static boolean checkStackSize(int index, String username) throws NullPointerException{
        int stackSize = stack.size();
        if (stackSize > index) {
            SpaceMarine el = stack.get(index);
            return username.equals(el.getOwner());
        }
        else return stackSize == index;
    }


    public Stack<SpaceMarine> getCollection() {
        try {
            readWriteLock.readLock().lock();
            return stack;
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    /**
     * check exist value SpaceMarine
     * @param Id
     * @return false or true
     */
    public boolean checkExist(Integer Id) {
        try {
            for (SpaceMarine spaceMarine: CollectionManager.getStack()) {
                if (spaceMarine.getId().equals(Id))
                    return true;
            }
            return false;
        } catch (NullPointerException e) {
            throw new EmptyStackException();
        }

    }
    public boolean checkExistAchievements(String achievement) {
        for (SpaceMarine spaceMarine: CollectionManager.getStack()) {
            if (spaceMarine.getAchievements() != null && spaceMarine.getAchievements().equals(achievement))
                return true;
        }
        return false;
    }
    /**
     * get items stack
     * @return stack
     */
    public static Stack<SpaceMarine> getStack() {
        return stack;
    }

    public static boolean stackIsEmpty() {
        return stack.empty();
    }
    /**
     * add the SpaceMarine class to the collection
     * @param spaceMarine
     */
    public void add(SpaceMarine spaceMarine) {
        try {
            readWriteLock.writeLock().lock();
            stack.push(spaceMarine);
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    /**
     * get information stack collection
     */
    public String info() throws EmptyStackException{
        try {
            readWriteLock.readLock().lock();
            return ("-----------------------------------"
                    + "\nТип коллекции: " + stack.getClass().getName()
                    + "\nДата инициализации: " + creationDate
                    + "\nКоличество элементов в коллекции: " + stack.size()
                    + "\n-----------------------------------\n");
        } finally {
            readWriteLock.readLock().unlock();
        }

    }
    /**
     * output all elements of the collection in string representation
     */
    public static String show() {
        try {
            readWriteLock.readLock().lock();
            StringBuilder information = new StringBuilder();
            for (SpaceMarine spaceMarine : stack) {
                information.append(spaceMarine.toString());
            }
            return information.toString();
        } finally {
            readWriteLock.readLock().unlock();
        }

    }
    /**
     * update the id of the element whose value is equal to the given one
     * @param sm
     * @param id
     */
    public void update(SpaceMarine sm, Integer id) {
        try {
            readWriteLock.writeLock().lock();
            for (SpaceMarine spaceMarine : stack) {
                if (spaceMarine.getId().equals(id)) {
                    spaceMarine.setName(sm.getName());
                    spaceMarine.setCoordinates(sm.getCoordinates());
                    spaceMarine.setHealth(sm.getHealth());
                    spaceMarine.setAchievements(sm.getAchievements());
                    spaceMarine.setWeaponType(sm.getWeaponType());
                    spaceMarine.setMeleeWeapon(sm.getMeleeWeapon());
                    spaceMarine.setChapter(sm.getChapter());
                }
            }

        } finally {
            readWriteLock.writeLock().unlock();
        }

    }
    /**
     * remove an item from the collection by id
     * @param id
     */
    public void removeById(Integer id) {
        try{
            readWriteLock.writeLock().lock();
            Iterator<SpaceMarine> i = stack.iterator();
            while (i.hasNext()) {
                SpaceMarine spaceMarine = i.next();
                    if (spaceMarine.getId().equals(id)) {
                        i.remove();
                        break;
                    }
            }
        } finally {
            readWriteLock.writeLock().unlock();
        }

    }
    /**
     * clear stack collection
     */
    public static void clear() {
        try {
            readWriteLock.writeLock().lock();
            stack.clear();
        } finally {
            readWriteLock.writeLock().unlock();
        }


    }
    /**
     * add b new element to b given position
     * @param index
     * @param element
     */
    public static void insertAt(int index, SpaceMarine element) throws IndexOutOfBoundsException{
        try {
            readWriteLock.writeLock().lock();
            stack.add(index, element);
        }
        finally {
            readWriteLock.writeLock().unlock();
        }
    }
    /**
     * add b new element if its value is less than the smallest element of this collection
     * @param spaceMarine
     */
//    public static void addIfMin(SpaceMarine spaceMarine) {
//        try {
//            readWriteLock.writeLock().lock();
//            SpaceMarineComparator comparator = new SpaceMarineComparator();
//            for (SpaceMarine spaceMarine1 : stack) {
//                if (comparator.compare(spaceMarine1, spaceMarine) == 1) {
//                    stack.add(spaceMarine1);
//                } else {
//                    collectionManagerLogger.info("Невозможно добавить элемент");
//                    break;
//                }
//            }
//        } finally {
//            readWriteLock.writeLock().unlock();
//        }
//
//    }
    /**
     * remove from the collection one item whose achievements field value is equivalent to the given one
     * @param achievements - field item of the class SpaceMarine
     */
    public static void removeAnyByAchievements(String achievements) {
        try {
            readWriteLock.writeLock().lock();
            Iterator<SpaceMarine> i = stack.iterator();
            while (i.hasNext()) {
                SpaceMarine spaceMarine = i.next();
                {
                    if (spaceMarine.getAchievements() != null && spaceMarine.getAchievements().equals(achievements)) {
                        i.remove();
                    }
                }
            }
        } finally {
            readWriteLock.writeLock().unlock();
        }

    }
    /**
     * sort items in natural order
     */
    public static void sort() {
        try {
            readWriteLock.writeLock().lock();
            Stack<SpaceMarine> tempStack = new Stack<>();
            while (!stack.isEmpty()) {
                SpaceMarine topInputStack = stack.pop();
                while (!tempStack.empty() && tempStack.peek().compareTo(topInputStack) > 0) {
                    SpaceMarine topTempStack = tempStack.pop();
                    stack.push(topTempStack);
                }
                tempStack.push(topInputStack);
            }
            stack = tempStack;
        } finally {
            readWriteLock.writeLock().unlock();
        }

    }

    /**
     * group the elements by coordinates value and print the number of elements in each group
     */
    public static Map<String, Integer> groupCountingByCoordinates() {
        try {
            readWriteLock.writeLock().lock();
            Map<Coordinates, Integer> countMap = new HashMap<>();
            for (SpaceMarine item : stack) {
                Coordinates coordinates = item.getCoordinates();
                countMap.put(coordinates, countMap.getOrDefault(coordinates, 0) + 1);
            }
            Map<String, Integer> groupCountMap = new HashMap<>();
            for (SpaceMarine item : stack) {
                Coordinates coordinates = item.getCoordinates();
                int count = countMap.getOrDefault(coordinates, 0);
                if (count > 0) {
                    String coordinateString = "Координата X: " + coordinates.getX() +
                            ", координата Y: " + coordinates.getY();
                    groupCountMap.put(coordinateString, count);
                    countMap.put(coordinates, 0);
                }
            }
            return groupCountMap;
        } finally {
            readWriteLock.writeLock().unlock();
        }

    }
}
