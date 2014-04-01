#!/bin/sh

android update project -p ./
android update project -p ./libraries/android-bootcamp-actionbarsherlock
android update project -p ./libraries/android-bootcamp-pulltorefresh

ant clean
ant product

