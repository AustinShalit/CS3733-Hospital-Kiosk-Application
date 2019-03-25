import com.diffplug.spotless.FormatterStep
import com.github.spotbugs.SpotBugsTask

plugins {
    java
    application
    jacoco
    checkstyle
    id("com.github.spotbugs") version "1.7.1"
    pmd
    id("com.diffplug.gradle.spotless") version "3.20.0"
    id("com.gradle.build-scan") version "2.2.1"
    idea
}

group = "edu.wpi.cs3733d18.onyx_owlmen"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    fun derby(name: String, version: String = "10.14.2.0") =
            create(group = "org.apache.derby", name = name, version = version)
    implementation(derby(name = "derby"))
    implementation(derby(name = "derbyclient"))
    implementation(derby(name = "derbytools"))

    fun junitJupiter(name: String, version: String = "5.4.1") =
            create(group = "org.junit.jupiter", name = name, version = version)
    testImplementation(junitJupiter(name = "junit-jupiter-api"))
    testImplementation(junitJupiter(name = "junit-jupiter-engine"))
    testImplementation(junitJupiter(name = "junit-jupiter-params"))
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

application {
    mainClassName = "edu.wpi.cs3733d18.onyx_owlmen.Main"
}

tasks.withType<Test> {
    useJUnitPlatform()
}

jacoco {
    toolVersion = "0.8.3"
}

tasks.withType<JacocoReport>().configureEach {
    reports {
        xml.isEnabled = true
        html.isEnabled = true
    }
}

checkstyle {
    toolVersion = "8.18"
}

spotbugs {
    toolVersion = "3.1.12"
    effort = "max"
    reportLevel = "high"
}

tasks.withType<SpotBugsTask> {
    reports {
        xml.isEnabled = false
        emacs.isEnabled = true
    }
    finalizedBy(task("${name}Report") {
        mustRunAfter(this@withType)
        doLast {
            this@withType
                    .reports
                    .emacs
                    .destination
                    .takeIf { it.exists() }
                    ?.readText()
                    .takeIf { !it.isNullOrBlank() }
                    ?.also { logger.warn(it) }
        }
    })
}

pmd {
    toolVersion = "6.12.0"
    isConsoleOutput = true
    ruleSetFiles = files("config/pmd/pmd-ruleset.xml")
    ruleSets = emptyList()
}

spotless {
    kotlinGradle {
        ktlint("0.31.0")
        trimTrailingWhitespace()
        indentWithSpaces()
        endWithNewline()
    }
    format("extraneous") {
        target("*.sh", "*.yml")
        trimTrailingWhitespace()
        indentWithSpaces()
        endWithNewline()
    }
    format("markdown") {
        target("*.md")
        // Default timeWhitespace() doesn't respect lines ending with two spaces for a tight line break
        // So we have to implement it ourselves
        class TrimTrailingSpaces : FormatterStep {
            override fun getName(): String = "trimTrailingSpaces"

            override fun format(rawUnix: String, file: File): String? {
                return rawUnix.split('\n')
                        .joinToString(separator = "\n", transform = this::formatLine)
            }

            fun formatLine(line: String): String {
                if (!line.endsWith(" ")) {
                    // No trailing whitespace
                    return line
                }
                if (line.matches(Regex("^.*[^ \t] {2}$"))) {
                    // Ends with two spaces - it's a tight line break, so leave it
                    return line
                }
                val endsWithMoreThanTwoSpaces = Regex("^(.*[^ \t]) {3,}^")
                val match = endsWithMoreThanTwoSpaces.matchEntire(line)
                if (match != null) {
                    // Ends with at least 3 spaces
                    // Trim the excess, but leave two spaces at the end for a tight line break
                    return match.groupValues[1] + "  "
                }
                if (line.endsWith(" ")) {
                    // Ends with a single space - remove it
                    return line.substring(0, line.length - 1)
                }
                // Not sure how we got here; every case should have been covered.
                // Print an error but do not change the line
                System.err.println("Could not trim whitespace from line '$line'")
                return line
            }
        }
        addStep(TrimTrailingSpaces())
        indentWithSpaces()
        endWithNewline()
    }
}

tasks.withType<Wrapper>().configureEach {
    gradleVersion = "5.3"
    distributionType = Wrapper.DistributionType.ALL
}
