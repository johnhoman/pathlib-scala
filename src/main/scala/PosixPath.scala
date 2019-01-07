package com.path.pathlib
import java.nio.file.{InvalidPathException}


class PosixPath(args: String*) {

    private val sep: String = "/"
    private val driveDelim: String = "://"
    private var drive = None: Option[String]
    private var root = None: Option[String]

    try {
        val Array(drive, root) = args(0).split(driveDelim)
        this.drive = Some(drive)
        this.root = Some(root)
    } catch {
        case e: scala.MatchError => {
            this.root = Some(args(0))
        }
        case fail: Exception => {
          fail.printStackTrace()
          throw new InvalidPathException(args.mkString(sep),
                                         "invalid path. cannot resolve")
        }
    }

    private val isAbsolute: Boolean = root.get.startsWith(sep)

    private val anchor: String = if (isAbsolute) sep else null

    val parts: Array[String] = {
        (root.get +: args.slice(1, args.length)).map(_.split(sep)).flatten.filter(
            x => x.length > 0
        ).toArray
    }

    def parent: PosixPath = {
        // need deferred evaluation to avoid "infinite" recursion
        if (parts.length > 1) {

        // TODO: ?? if path **has no** parent
            val pieces = parts.slice(0, parts.length - 1)
            var base = (new PosixPath(pieces: _*)).toString()
            if (anchor != null) {
                base = anchor + base
            }
            if (drive.isDefined) {
                base = drive.get + driveDelim + base
            }
            new PosixPath(base)
        } else {
            throw new InvalidPathException(this.toString(),
                                           "path has no parent")
        }
      }

    def joinpath(path: String): PosixPath = {
        return new PosixPath(this.toString(), path)
    }

    def /(that: String): PosixPath = {
        return this.joinpath(that)
    }

    def /(that: PosixPath): PosixPath = {
        return this.joinpath(that.toString())
    }

    def joinpath(path: PosixPath): PosixPath = {
        if (path.isAbsolute) {
            throw new InvalidPathException(
                path.toString(),
                "path cannot be absolute or have drive prefix")
        } else {
            return this.joinpath(path.toString())
        }
    }

    override def toString(): String = {
        var str: String = parts.mkString(sep)

        if (anchor != null) {
            str = anchor + str
        }
        if (drive.isDefined) {
            str = drive.get + driveDelim + str
        }
        return str
    }
}
