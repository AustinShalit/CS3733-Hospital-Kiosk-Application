import com.diffplug.spotless.FormatterStep
import com.diffplug.spotless.extra.wtp.EclipseWtpFormatterStep
import com.github.spotbugs.SpotBugsTask
import org.apache.tools.ant.taskdefs.condition.Os

plugins {
    java
    application
    jacoco
    checkstyle
    id("com.github.spotbugs") version "1.7.1"
    pmd
    id("com.diffplug.gradle.spotless") version "3.20.0"
    id("org.ajoberstar.reckon") version "0.9.0"
    id("com.github.johnrengelman.shadow") version "5.0.0"
    id("com.gradle.build-scan") version "2.2.1"
    idea
}

group = "edu.wpi.cs3733.d19.teamO"

reckon {
    scopeFromProp()
    snapshotFromProp()
}

repositories {
    mavenCentral()
    jcenter()
    maven("http://first.wpi.edu/FRC/roborio/maven/release")
}
allprojects {
    apply {
        plugin("java")
        plugin("jacoco")
        plugin("checkstyle")
        plugin("pmd")
        plugin("com.github.spotbugs")
        plugin("com.github.johnrengelman.shadow")
    }

    repositories {
        mavenCentral()
        jcenter()
        maven("https://ravana.dyn.wpi.edu/maven")
    }

    dependencies {
        implementation(group = "edu.wpi.first.ntcore", name = "ntcore-java", version = "2019.+")
        runtimeOnly(group = "edu.wpi.first.ntcore", name = "ntcore-jni", version = "2019.+", classifier = "osxx86-64")
        runtimeOnly(group = "edu.wpi.first.ntcore", name = "ntcore-jni", version = "2019.+", classifier = "windowsx86-64")
        runtimeOnly(group = "edu.wpi.first.ntcore", name = "ntcore-jni", version = "2019.+", classifier = "windowsx86")
        runtimeOnly(group = "edu.wpi.first.ntcore", name = "ntcore-jni", version = "2019.+", classifier = "linuxx86-64")

        implementation(group = "edu.wpi.first.wpiutil", name = "wpiutil-java", version = "2019.+")

        implementation(group = "com.calendarfx", name = "view", version = "8.5.0")
        implementation(group = "com.google.code.gson", name = "gson", version = "2.8.5")
        implementation(group = "com.google.guava", name = "guava", version = "27.1-jre")
        implementation(group = "com.google.inject", name = "guice", version = "4.2.2")
        implementation(group = "com.google.inject.extensions", name = "guice-assistedinject", version = "4.2.2")
        implementation(group = "com.jfoenix", name = "jfoenix", version = "8.0.8")
        implementation(group = "com.opencsv", name = "opencsv", version = "4.5")
        implementation(group = "de.jensd", name = "fontawesomefx", version = "8.9")
        implementation(group = "io.github.typhon0", name = "AnimateFX", version = "1.2.1")
        implementation(group = "net.aksingh", name = "owm-japis", version = "2.5.2.3")
        implementation(group = "net.kurobako", name = "gesturefx", version = "0.3.0")
        implementation(group = "net.sf.sociaal", name = "freetts", version = "1.2.2")
        implementation(group = "org.controlsfx", name = "controlsfx", version = "8.40.14")
        implementation(group = "org.fxmisc.easybind", name = "easybind", version = "1.0.3")
        implementation(group = "me.xdrop", name = "fuzzywuzzy", version = "1.2.0")
        implementation(group = "javax.measure", name = "unit-api", version = "1.0")
        implementation(group = "com.twilio.sdk", name = "twilio", version = "7.37.2")
        implementation(group = "si.uom", name = "si-units", version = "0.9")
        implementation(group = "systems.uom", name = "systems-common", version = "0.9")

            compile(fileTree("libs"))

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

            fun testFx(name: String, version: String = "4.0.15-alpha") =
                    create(group = "org.testfx", name = name, version = version)
            testImplementation(testFx(name = "testfx-core"))
            testImplementation(testFx(name = "testfx-junit5"))
            testRuntime(testFx(name = "openjfx-monocle", version = "8u76-b04"))
        }

        tasks.withType<JavaCompile> {
            sourceCompatibility = JavaVersion.VERSION_1_8.toString()
            targetCompatibility = JavaVersion.VERSION_1_8.toString()
        }

        tasks.withType<Test> {
            useJUnitPlatform {
                maxParallelForks = Math.max(1, Runtime.getRuntime().availableProcessors() - 2)
                if (project.hasProperty("skipUI")) {
                    excludeTags("UI")
                }
            }
            if (!project.hasProperty("visibleUiTests")) {
                jvmArgs = listOf(
                        "-Djava.awt.headless=true",
                        "-Dtestfx.robot=glass",
                        "-Dtestfx.headless=true",
                        "-Dprism.order=sw",
                        "-Dprism.text=t2k"
                )
            }
            finalizedBy("jacocoTestReport")
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
            ruleSetFiles = files("$rootDir/config/pmd/pmd-ruleset.xml")
            ruleSets = emptyList()
        }
    }

    application {
        mainClassName = "edu.wpi.cs3733.d19.teamO.Main"
    }

    spotless {
        kotlinGradle {
            ktlint("0.31.0")
            target("**/*.gradle.kts")
            trimTrailingWhitespace()
            indentWithSpaces()
            endWithNewline()
        }
        format("xml") {
            target("**/*.fxml")
            trimTrailingWhitespace()
            indentWithSpaces()
            endWithNewline()
            eclipseWtp(EclipseWtpFormatterStep.XML).configFile(files("config/spotless/xmlformat.prefs"))
        }
        format("extraneous") {
            target("**/*.sh", "**/*.yml", "**/*.prefs", "**/*.properties")
            trimTrailingWhitespace()
            indentWithSpaces()
            endWithNewline()
        }
        format("markdown") {
            target("**/*.md")
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

    tasks.register<Delete>("deleteDatabase") {
        group = "build"
        description = "Delete the application database for debugging"

        val directoryName = "Onyx Owlmen Kiosk"
        when {
            Os.isFamily(Os.FAMILY_WINDOWS) -> delete(System.getenv("APPDATA") +
                    File.separator +
                    directoryName)
            Os.isFamily(Os.FAMILY_MAC) -> delete(System.getProperty("user.home") +
                    "/Library/" +
                    directoryName)
            else -> delete(System.getProperty("user.home") +
                    File.separator +
                    directoryName)
        }
    }

    tasks.withType<Wrapper>().configureEach {
        gradleVersion = "5.4"
        distributionType = Wrapper.DistributionType.ALL
    }