package tsi.too;

import tsi.too.controller.MenuController;

public class Application {
    public static void main(String[] args) {
        MenuController.getInstance().menu();
    }
}