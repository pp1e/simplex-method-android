//package ru.ac.uniyar.simplexmethodtaskssolver.utils
//
//import java.io.File
//import java.io.FileFilter
//import java.util.*
//import javax.swing.filechooser.FileFilter
//
//class TxtFileFilter : FileFilter {
//
//    override fun accept(file: File): Boolean {
//        return if (file.isDirectory) {
//            true
//        } else {
//            val name = file.name
//            name.lowercase(Locale.getDefault()).endsWith(".txt")
//        }
//    }
//
//    override fun getDescription(): String {
//        return "*.txt"
//    }
//}
