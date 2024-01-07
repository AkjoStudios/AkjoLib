<!--suppress HtmlDeprecatedAttribute -->
<div align="center">
<img src="https://img.shields.io/codefactor/grade/github/AkjoStudios/AkjoLib/main?style=flat-square&label=CodeFactor Grade" alt="CodeFactor Grade" />
<img src="https://img.shields.io/github/v/release/AkjoStudios/AkjoLib?style=flat-square&label=Latest%20Release" alt="Latest Release" />
<img src="https://img.shields.io/github/license/AkjoStudios/AkjoLib?style=flat-square&label=License" alt="License" />
<h1>📕 AkjoLib</h1>
<p>
    AkjoLib is a library for Java 21 containing various utilities and classes helpful for doing functional programming in Java.
</p>
<p>
    ⚠️ It is not fully tested yet and still in development, so use at your own risk. It will be for the most part when it reaches version 1.0.0.
</p>
<h2>History and Pragmatica</h2>
<p>
    This library is the successor to my old personal library that had gone through many iterations and was only ever used by me.
</p>
<p>
    I decided to make a new library that is more generic and can be used by others as well and finally extend it's functional aspects and update it to Java 21.
</p>
<hr/>
<p>
    While I built this library I came across the library <a href="https://github.com/siy/pragmatica">Pragmatica</a> which I combined with concepts of my old library. Improving and writing/copying line by line until most of the ideas from my previous library and the features of Pragmatica were implemented.
</p>
<p>
    Now to be on the safe side I will not be releasing this library under the MIT license as I wanted to, but instead under the Apache 2.0 license as Pragmatica is licensed under it as well.
</p>
<p>
    The only thing that I don't like about the Apache 2.0 license is that it requires a JavaDoc comment for every class that states the license and copyright and that others including my own projects will need to license it under the same license as well (including my own projects), but I will need to live with that I guess, mainly because now I don't want to throw out my additional work to completely rework the library and implement the exact same thing again.
</p>
<p>
    I think the work that is provided by Pragmatica is very well done and I honestly don't have the resources to do it better.
</p>
<p>
    I will refrain from updating the code to be on par with Pragmatica and all new code will be my own and will obviously <br/> only contain the copyright from me (Lukas Küffer / <a href="https://github.com/Akjo03">Akjo03</a>).
</p>
<p>
    Credit for all classes and interfaces inspired by Pragmatica goes to <a href="https://github.com/siy">Sergiy Yevtushenko (siy)</a> and all the other contributors to <a href="https://github.com/siy/pragmatica">Pragmatica</a>.
</p>
<h2>✅ Implemented Features</h2>
<ul>
    <li>Result interface for handling errors and exceptions with causes replacing regular exceptions.</li>
    <li>Option interface for handling null values.</li>
    <li>Tuple interfaces for storing and mapping multiple values.</li>
    <li>Validator interface for validating values, including various implementations and ValidatorGroup for combining multiple validators.</li>
    <li>Builders and constructors for building and validating data classes and records.</li>
    <li>Promises for asynchronous programming using the Result interface.</li>
    <li>Various other functional interfaces for use with lambdas.</li>
    <li>Generic Tree and MutableTree classes for storing data in a tree structure.</li>
    <li>Special FilteringSet implementation for storing data in a Set and filtering it using another Set.</li>
    <li>Various helper classes/interfaces for setting up a default ObjectMapper using Jackson.</li>
    <li>Math related classes for Vector2, Vector3 and ranges.</li>
    <li>ConsoleColor class for coloring console output.</li>
</ul>
<h2>💡 Planned Features</h2>
<ul>
    <li>Finishing tests and continuously adding more for coming features.</li>
    <li>Better asynchronous support for functional programming other than promises.</li>
    <li>Various other functional interfaces for better functional programming support.</li>
    <li>More math related classes for matrices and quaternions.</li>
    <li>More generic data structures for trees, graphs, etc.</li>
    <li>More helper classes for creating command-line interfaces.</li>
    <li>Many more features and improvements to existing classes/interfaces!</li>
</ul>
</div>

--------

<div align="center">
<h2>Usage and Installation</h2>
</div>

```xml
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>
```

```xml
<dependency>
    <groupId>com.github.AkjoStudios</groupId>
    <artifactId>AkjoLib</artifactId>
    <version>0.3.0</version>
</dependency>
```
