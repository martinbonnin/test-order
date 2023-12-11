plugins {
  id("org.jetbrains.kotlin.jvm").version("1.9.20")
  id("com.apollographql.apollo3").version("3.8.2")
}


dependencies {
  testImplementation(kotlin("test"))
  implementation("com.apollographql.apollo3:apollo-runtime")
  testImplementation("com.apollographql.apollo3:apollo-mockserver")
  testImplementation("com.apollographql.apollo3:apollo-normalized-cache")
}

apollo {
  service("service") {
    packageName.set("com.example")
  }
}
