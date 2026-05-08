<?php
session_start(); //starts the session

if (!isset($_SESSION['user_id'])) { //checks if user is logged in
    header("Location: index.php"); //redirects to index.php 
    exit();
}
?>