package com.example.spendsmart;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Categories {
    public static ArrayList<Category> expenseCategories = new ArrayList<>();
    public static ArrayList<Category> incomeCategories = new ArrayList<>();

    public static ArrayList<Category> goalCategories = new ArrayList<>();

    public static ArrayList<Category> budgetCategories = new ArrayList<>();

    static {
        expenseCategories.add(new Category("House Rent","#ff0000","cat_house_rent"));
        expenseCategories.add(new Category("Shopping","#6100e0","cat_shopping"));
        expenseCategories.add(new Category("Food and Drinks","#fcbe03","cat_food_drink"));
        expenseCategories.add(new Category("Clothes and Shoes","#6100e0","cat_clothes_shoes"));
        expenseCategories.add(new Category("Investments","#ff7b00","cat_env"));
        expenseCategories.add(new Category("Medicines","#36998d","cat_health"));
        expenseCategories.add(new Category("Pets","#ff00a2","cat_pets"));
        expenseCategories.add(new Category("Fees","#ff0000","cat_money"));
        expenseCategories.add(new Category("Groceries","#6100e0","cat_shopping"));
        expenseCategories.add(new Category("Utilities","#fcbe03","cat_util"));
        expenseCategories.add(new Category("Transportation and Fuel","#ff7b00","cat_vehicle"));
        expenseCategories.add(new Category("Housing","#fcbe03","cat_house_rent"));
        expenseCategories.add(new Category("Insurance","#6100e0","cat_money"));
        expenseCategories.add(new Category("Vehicle","#0074e8","cat_vehicle"));
        expenseCategories.add(new Category("Doctor","#36998d","cat_health"));
        expenseCategories.add(new Category("Loans","#6100e0","cat_money"));
        expenseCategories.add(new Category("Entertainment","#ff00a2","cat_entertainment"));
        expenseCategories.add(new Category("Internet and Phone","#0074e8","cat_phone_internet"));
        expenseCategories.add(new Category("Taxes","#914d0d","cat_money"));
        expenseCategories.add(new Category("Mortgage","#914d0d","cat_house_rent"));
        expenseCategories.add(new Category("Others","#000000","cat_others"));

        incomeCategories.add(new Category("Salary","#36998d","cat_money"));
        incomeCategories.add(new Category("Rental Income","#ff0000","cat_house_rent"));
        incomeCategories.add(new Category("Investment","#914d0d","cat_env"));
        incomeCategories.add(new Category("Lottery","#6100e0","cat_money"));
        incomeCategories.add(new Category("Gifts","#ff00a2","cat_gift"));
        incomeCategories.add(new Category("Sale","#fcbe03","cat_shopping"));
        incomeCategories.add(new Category("Loans and Grants","#ff7b00","cat_money"));
        incomeCategories.add(new Category("Other Sources","#000000","cat_others"));


        goalCategories.add(new Category("Retirement","#36998d","cat_money"));
        goalCategories.add(new Category("Rent","#ff0000","cat_house_rent"));
        goalCategories.add(new Category("Housing","#914d0d","cat_env"));
        goalCategories.add(new Category("Education","#6100e0","cat_money"));
        goalCategories.add(new Category("Travel","#6100e0","cat_vehicle"));
        goalCategories.add(new Category("Healthcare","#6100e0","cat_health"));
        goalCategories.add(new Category("Other","#000000","cat_others"));



    }
}
