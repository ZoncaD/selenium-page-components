package io.github.zoncad.example.angularmaterial;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class ComponentCard {
    WebElement root;

    public ComponentCard(WebElement root) {
        this.root = root;
    }

    public String getTitle() {
        return root.findElement(By.cssSelector("div.docs-component-category-list-card-title")).getText();
    }

    public String getSummary() {
        return root.findElement(By.xpath(".//div[contains(concat(' ',normalize-space(@class),' '),' docs-component-category-list-card-summary ')]"))
                .getText();
    }
}
