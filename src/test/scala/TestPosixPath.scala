import com.path.PathLib
import org.scalatest.FeatureSpec


class TestPosixPath extends FeatureSpec {

    info("tests for posix path manipulation")


    feature("constructor") {
        scenario("absolute path") {
            val testpath = "/home/jack/Downloads"
            val expected = Array("home", "jack", "Downloads")
            val path: PosixPath = new PosixPath(testpath)
            assert(path.parts.sameElements(expected))
        }
        scenario("path with drive") {
            val testDrivePath = "adl://data-science/scratch/jack/cos"
            val expected1 = Array("data-science", "scratch", "jack", "cos")
            val other: PosixPath = new PosixPath(testDrivePath)
            assert(other.parts.sameElements(expected1))
            assert(other.toString() == testDrivePath)
        }
    }

    feature("joinpath") {
        scenario("simple join - absolute path join directory") {
            val testpath = "/home/jack"
            val extended = "Downloads"
            val expected = Array("home", "jack", "Downloads")

            val path: PosixPath = new PosixPath(testpath)
            val full: PosixPath = path.joinpath(extended)
            assert(full.parts.sameElements(expected))
        }
        scenario("simple join - overloaded / operator") {
            val testpath = "/home/jack"
            val extended = "Downloads"
            val expected = Array("home", "jack", "Downloads")
            val path: PosixPath = new PosixPath(testpath)
            val full: PosixPath = path / extended
            assert(full.parts.sameElements(expected))
        }

    }

    feature("parent path") {
        scenario("simple path - single file") {
            val testpath = "/home/jack/Downloads/spark_2.11.tgz"
            val expected = Array("home", "jack", "Downloads")
            val parent: PosixPath = (new PosixPath(testpath)).parent
            assert(parent.parts == expected)
        }
        scenario("path with drive") {
            val testpath = "local:///home/jack/Downloads/spark_2.11.tgz"
            val expected = Array("home", "jack", "Downloads")
            val parent: PosixPath = (new PosixPath(testpath)).parent
            assert(parent.parts == expected)
            assert(parent.toString == "local:///home/jack/Downloads")
        }
    }
}
