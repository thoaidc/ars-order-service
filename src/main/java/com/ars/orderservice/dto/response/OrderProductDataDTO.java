package com.ars.orderservice.dto.response;

public class OrderProductDataDTO {
    private boolean customizable;
    private String designFile;
    private String selectedOptions;

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

    public String getSelectedOptions() {
        return selectedOptions;
    }

    public void setSelectedOptions(String selectedOptions) {
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
