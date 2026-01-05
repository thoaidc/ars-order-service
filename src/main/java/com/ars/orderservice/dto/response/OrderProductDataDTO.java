package com.ars.orderservice.dto.response;

import java.util.ArrayList;
import java.util.List;

public class OrderProductDataDTO {
    private boolean customizable;
    private String designFile;
    private List<Option> selectedOptions = new ArrayList<>();

    public boolean isCustomizable() {
        return customizable;
    }

    public boolean getCustomizable() {
        return customizable;
    }

    public void setCustomizable(boolean customizable) {
        this.customizable = customizable;
    }

    public String getDesignFile() {
        return designFile;
    }

    public void setDesignFile(String designFile) {
        this.designFile = designFile;
    }

    public List<Option> getSelectedOptions() {
        return selectedOptions;
    }

    public void setSelectedOptions(List<Option> selectedOptions) {
        this.selectedOptions = selectedOptions;
    }

    public static class Option {
        private Integer id;
        private Integer selectedOptionValueId;
        private String selectedOptionValueImage;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getSelectedOptionValueId() {
            return selectedOptionValueId;
        }

        public void setSelectedOptionValueId(Integer selectedOptionValueId) {
            this.selectedOptionValueId = selectedOptionValueId;
        }

        public String getSelectedOptionValueImage() {
            return selectedOptionValueImage;
        }

        public void setSelectedOptionValueImage(String selectedOptionValueImage) {
            this.selectedOptionValueImage = selectedOptionValueImage;
        }
    }
}
