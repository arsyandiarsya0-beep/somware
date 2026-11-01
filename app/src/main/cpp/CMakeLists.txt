cmake_minimum_required(VERSION 3.22.1)
project(arxware)
add_library(arxware SHARED arxware.cpp)
target_link_libraries(arxware android log dl)
