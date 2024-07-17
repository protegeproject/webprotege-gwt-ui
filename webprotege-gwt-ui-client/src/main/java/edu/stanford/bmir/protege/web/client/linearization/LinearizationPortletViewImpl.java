package edu.stanford.bmir.protege.web.client.linearization;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.form.complexcheckbox.CheckBoxConfig;
import edu.stanford.bmir.protege.web.client.form.complexcheckbox.CheckboxValue;
import edu.stanford.bmir.protege.web.client.form.complexcheckbox.ThreeStateCheckbox;
import edu.stanford.bmir.protege.web.client.form.input.CheckBox;
import edu.stanford.bmir.protege.web.shared.linearization.LinearizationSpecification;
import edu.stanford.bmir.protege.web.shared.linearization.WhoficEntityLinearizationSpecification;
import org.checkerframework.checker.units.qual.C;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.logging.Logger;

public class LinearizationPortletViewImpl extends Composite implements LinearizationPortletView {
    Logger logger = java.util.logging.Logger.getLogger("LinearizationPortletViewImpl");

    private LinearizationPortletView.LinearizationPaneChangedHandler linearizationPaneChangedHandler = () -> {};

    private WhoficEntityLinearizationSpecification specification;

    @UiField
    HTMLPanel paneContainer;

    @UiField
    protected FlexTable flexTable;


    private static LinearizationPortletViewImplUiBinder ourUiBinder = GWT.create(LinearizationPortletViewImplUiBinder.class);

    @Inject
    public LinearizationPortletViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void setWidget(IsWidget w) {
        paneContainer.clear();
        paneContainer.add(w);
    }

    @Override
    public void dispose() {
        flexTable.removeAllRows();
    }

    @Override
    public void setWhoFicEntity(WhoficEntityLinearizationSpecification specification) {
        try {
            this.specification = specification;
            flexTable.setBorderWidth(1);
            flexTable.setWidget(0, 0, new Label("Linearization"));
            flexTable.setWidget(0, 1, new Label("Is part of?"));
            flexTable.setWidget(0, 2, new Label("Is grouping?"));
            flexTable.setWidget(0, 3, new Label("Linearization parent"));
            flexTable.setWidget(0, 4, new Label("Sorting Label"));
            flexTable.setWidget(0, 5, new Label("Comments"));
            flexTable.getRowFormatter().setStyleName(0, "customRowStyle");

            for(int i = 0; i < this.specification.getLinearizationSpecifications().size(); i ++ ){
                //LinearizationSpecificationRowPresenter row = new LinearizationSpecificationRowPresenter(this.specification.getLinearizationSpecifications().get(i));
                LinearizationSpecification linearizationSpecification = this.specification.getLinearizationSpecifications().get(i);

                flexTable.setWidget(i+1, 0, new Label(linearizationSpecification.getLinearizationView()));
                CheckBox isIncluded = new CheckBox();
                isIncluded.setValue(getValueOutOfString(linearizationSpecification.getIsIncludedInLinearization()));

                CheckBox isPartOfGroup = new CheckBox();
                isPartOfGroup.setValue(getValueOutOfString(linearizationSpecification.getIsGrouping()));

                CheckboxValue unknown = new CheckboxValue(UNKNOWN_SVG, "UNKNOWN");
                LinkedList<CheckboxValue> e = new LinkedList<>();
                e.add(new CheckboxValue(X_SVG, "FALSE"));
                e.add(new CheckboxValue(CHECK_SVG, "TRUE"));
                e.add(unknown);

                CheckBoxConfig checkBoxConfig = new LinearizationCheckboxConfig(e);

                flexTable.setWidget(i+1, 1, isIncluded);
                flexTable.setWidget(i+1, 2, new ThreeStateCheckbox(checkBoxConfig, unknown));
                flexTable.setWidget(i+1, 3, new Label(linearizationSpecification.getLinearizationParent()));
                flexTable.setWidget(i+1, 4, new Label(linearizationSpecification.getSortingLabel()));
                flexTable.setWidget(i+1, 5, new Label(linearizationSpecification.getCodingNote()));

            }
            logger.info("ALEX " + specification);
        }catch (Exception e) {
            logger.info("ALEX EROARE " + e.getMessage() + " stacktrace " + Arrays.toString(e.getStackTrace()));
        }

    }


    interface LinearizationPortletViewImplUiBinder extends UiBinder<HTMLPanel, LinearizationPortletViewImpl> {

    }


    private Boolean getValueOutOfString(String s){

        return "true".equalsIgnoreCase(s);
    }

    private final String CHECK_SVG = "<svg width=\"14px\" height=\"14px\">\n" +
            "                <rect class=\"wp-checkbox__input__box\"\n" +
            "                      x=\"1\" y=\"1\"\n" +
            "                      width=\"12\" height=\"12\"\n" +
            "                      rx=\"2\" ry=\"2\"/>\n" +
            "                <path class=\"wp-checkbox__input__check-mark\"\n" +
            "                      d=\"M3.5 7\n" +
            "                         L6 11\n" +
            "                         L10.5 3.5\"\n" +
            "                      fill=\"none\"\n" +
            "                      stroke-linejoin=\"round\"\n" +
            "                      stroke-linecap=\"round\"/>\n" +
            "            </svg>";
    private final String X_SVG = "<svg fill=\"#ff2424\" viewBox=\"0 0 32 32\" xmlns=\"http://www.w3.org/2000/svg\" stroke=\"#ff2424\"><g id=\"SVGRepo_bgCarrier\" stroke-width=\"0\"></g><g id=\"SVGRepo_tracerCarrier\" stroke-linecap=\"round\" stroke-linejoin=\"round\"></g><g id=\"SVGRepo_iconCarrier\"> <path d=\"M18.8,16l5.5-5.5c0.8-0.8,0.8-2,0-2.8l0,0C24,7.3,23.5,7,23,7c-0.5,0-1,0.2-1.4,0.6L16,13.2l-5.5-5.5 c-0.8-0.8-2.1-0.8-2.8,0C7.3,8,7,8.5,7,9.1s0.2,1,0.6,1.4l5.5,5.5l-5.5,5.5C7.3,21.9,7,22.4,7,23c0,0.5,0.2,1,0.6,1.4 C8,24.8,8.5,25,9,25c0.5,0,1-0.2,1.4-0.6l5.5-5.5l5.5,5.5c0.8,0.8,2.1,0.8,2.8,0c0.8-0.8,0.8-2.1,0-2.8L18.8,16z\"></path> </g></svg>";

    private final String UNKNOWN_SVG = "<svg viewBox=\"0 0 24 24\" fill=\"none\" xmlns=\"http://www.w3.org/2000/svg\"><g id=\"SVGRepo_bgCarrier\" stroke-width=\"0\"></g><g id=\"SVGRepo_tracerCarrier\" stroke-linecap=\"round\" stroke-linejoin=\"round\"></g><g id=\"SVGRepo_iconCarrier\"> <path d=\"M12 19H12.01M8.21704 7.69689C8.75753 6.12753 10.2471 5 12 5C14.2091 5 16 6.79086 16 9C16 10.6565 14.9931 12.0778 13.558 12.6852C12.8172 12.9988 12.4468 13.1556 12.3172 13.2767C12.1629 13.4209 12.1336 13.4651 12.061 13.6634C12 13.8299 12 14.0866 12 14.6L12 16\" stroke=\"#828282\" stroke-width=\"2\" stroke-linecap=\"round\" stroke-linejoin=\"round\"></path> </g></svg>";

}
