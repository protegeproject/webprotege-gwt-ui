package edu.stanford.bmir.protege.web.client.form.complexcheckbox;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

public interface ConfigurableCheckBoxResourceBundle extends ClientBundle {


    ConfigurableCheckBoxResourceBundle INSTANCE = GWT.create(ConfigurableCheckBoxResourceBundle.class);


    @Source("configurableCheckbox.css")
    CheckBoxCss style();

    interface CheckBoxCss extends CssResource {

        @ClassName("wp-checkbox-configurable")
        String checkBox();

        @ClassName("wp-checkbox-configurable__input")
        String checkBox__input();

        @ClassName("wp-checkbox-configurable__input__box")
        String checkBox__input__box();

        @ClassName("wp-checkbox-configurable__input__check-mark")
        String checkBox__checkMark();

        @ClassName("wp-checkbox-configurable__label")
        String checkBox__label();

        @ClassName("wp-checkbox-configurable__input--checked")
        String checkBox__input__checked();

        @ClassName("configurableCheckboxContainer")
        String getConfigurableCheckboxContainer();
    }

}
