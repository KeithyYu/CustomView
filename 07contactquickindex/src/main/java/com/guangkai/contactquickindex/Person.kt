package com.guangkai.contactquickindex

class Person(var name: String) {
    var pinyin: String = PinYinUtils.getPinYin(name)

    override fun toString(): String {
        return "Person{" +
                "name='" + name + '\'' +
                ", pinyin='" + pinyin + '\'' +
                '}'
    }
}