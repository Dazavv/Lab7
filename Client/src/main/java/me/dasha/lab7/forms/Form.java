package me.dasha.lab7.forms;


import me.dasha.lab7.exp.InvalidFormExp;

/**
 * Абстрактный класс для пользовательских форм ввода
 * @param <T> класс формы
 */
public abstract class Form<T>{
    public abstract T build() throws InvalidFormExp;
}
