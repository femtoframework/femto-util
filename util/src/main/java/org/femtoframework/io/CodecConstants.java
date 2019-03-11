package org.femtoframework.io;

/**
 * Codec常量
 *
 * @author fengyun
 */
public interface CodecConstants
{
    //UNDER 255
    int TYPE_NULL = 0;
    int TYPE_OBJECT = 1;
    int TYPE_BYTE = 2;
    int TYPE_BOOLEAN = 3;
    int TYPE_CHARACTER = 4;
    int TYPE_SHORT = 5;
    int TYPE_INTEGER = 6;
    int TYPE_LONG = 7;
    int TYPE_FLOAT = 8;
    int TYPE_DOUBLE = 9;
    int TYPE_STRING = 10;
    int TYPE_BYTE_ARRAY = 11;
    int TYPE_CHAR_ARRAY = 12;
    int TYPE_STRING_ARRAY = 13;

    //Streamable
    int TYPE_STREAMABLE = 14;

    //只传类名
    int TYPE_CLASS = 15;

    int TYPE_MARSHALLABLE = 16;

    int TYPE_OBJECT_ARRAY = 17;
    int TYPE_EXTERNALIZABLE = 18;

    int MAX_ARRAY_LENGTH = 64 * 1024;
    int MAX_STRING_LENGTH = 1024 * 1024;



    int BYTE_BUFFER_SIZE = 1024;
    int CHAR_BUFFER_SIZE = 512;
    
}
