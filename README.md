# OkFaye #

Faye Client implementation built on okhttp-ws

[![](https://jitpack.io/v/mariotaku/OkFaye.svg)](https://jitpack.io/#mariotaku/OkFaye)

## Usage ##

### Add to your project ###

Add the JitPack repository to your build file

````
repositories {
    ...
    maven { url "https://jitpack.io" }
}
````

Add the dependency

````
dependencies {
    compile 'com.github.mariotaku:OkFaye:x.x' // See latest release
}
````

### Use the library ###

````java
OkHttpClient client = (...);
Request request = (...);
WebSocketCall call = WebSocketCall.create(client, request);
Faye faye = Faye.create(client, call);
faye.subscribe("/your/channel", message -> {...});
Object sendMessage = (...); // String or model annotated by @JsonObject
faye.publish("target/channel", sendMessage, response -> {...});
````

### Additional features ###

#### Extension support ####

````java
faye.setExtension(...); // Must be annotated by @JsonObject
````

#### Error handling ####
````java
faye.setErrorListener((e, code, reason) -> {...});// Called when any error occured
````

## Donate ##

PayPal & AliPay: `String.format("%s@%s", "mariotaku.lee", "gmail.com");`

Bitcoin: `1BJnubdQWmHYpugRg3DGCwjpLd2TAL9nVX`

## License ##

(The MIT License)

````
Copyright (c) 2016 Mariotaku Lee <mariotaku.lee@gmail.com>

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
````
