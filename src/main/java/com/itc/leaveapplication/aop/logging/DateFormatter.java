package com.itc.leaveapplication.aop.logging;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

import java.beans.PropertyEditorSupport;
import java.text.Format;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.function.Function;

@ControllerAdvice
public class DateFormatter {

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {
        if (webDataBinder.getTarget() != null
            && webDataBinder.getTarget() instanceof Collection<?>) {
            //webDataBinder.addValidators(collectionValidator);
        }

        webDataBinder.registerCustomEditor(
            LocalDate.class,
            new Editor<>(
                text -> LocalDate.parse(text, DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                DateTimeFormatter.ofPattern("yyyy-MM-dd").toFormat()));

        webDataBinder.registerCustomEditor(
            LocalDateTime.class,
            new Editor<>(
                text -> LocalDateTime.parse(text, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").toFormat()));

        webDataBinder.registerCustomEditor(
            LocalTime.class,
            new Editor<>(
                text -> LocalTime.parse(text, DateTimeFormatter.ofPattern("HH:mm:ss")),
                DateTimeFormatter.ofPattern("HH:mm:ss").toFormat()));
        //webDataBinder.registerCustomEditor(String.class, new StringTrimmerEditor(true));*


    }

    private static class Editor<T> extends PropertyEditorSupport {

        private final Function<String, T> parser;
        private final Format format;

        public Editor(Function<String, T> parser, Format format) {

            this.parser = parser;
            this.format = format;
        }

        public String getAsText() {

            return format.format((T) getValue());
        }

        public void setAsText(String text) {

            setValue(this.parser.apply(text));
        }
    }
}
