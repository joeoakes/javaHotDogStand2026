/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;
/**
 *
 * @author Gokhan
 */
public class Customer 
{
    private int ID;
    private String firstName;
    private String lastName;
    private String favoriteMeal;
    
    public Customer(int ID, String firstName, String lastName, String favoriteMeal)
    {
        this.ID = ID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.favoriteMeal = favoriteMeal;
    }

    public int getID() {
        return ID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFavoriteMeal() {
        return favoriteMeal;
    }

    @Override
    public String toString() {
        return "Customer{" + "ID=" + ID + ", firstName=" + firstName + ", lastName=" + lastName + ", favoriteMeal=" + favoriteMeal + '}';
    }
}
