package com.ssafy.mmart.service

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.DeleteObjectRequest
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import com.google.zxing.BarcodeFormat
import com.google.zxing.client.j2se.MatrixToImageWriter
import com.google.zxing.qrcode.QRCodeWriter
import com.ssafy.mmart.exception.not_found.ItemNotFoundException
import com.ssafy.mmart.exception.not_found.PhotoNotFoundException
import com.ssafy.mmart.exception.not_found.UserNotFoundException
import com.ssafy.mmart.repository.ItemRepository
import com.ssafy.mmart.repository.UserRepository
import marvin.image.MarvinImage
import org.marvinproject.image.transform.scale.Scale
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.server.ResponseStatusException
import java.awt.image.BufferedImage
import java.io.*
import java.net.URL
import java.util.*
import javax.imageio.ImageIO


@Service
class AmazonS3Service @Autowired constructor(
    private val amazonS3: AmazonS3,

    @Value("\${cloud.aws.s3.bucket}")
    private val bucket: String,

    @Value("\${aws-cloud.aws.s3.bucket.url}")
    private val bucketUrl: String,

    val getCartService: GetCartService,
    val itemRepository: ItemRepository,
    val userRepository: UserRepository,
) {

    // QR코드 이미지 생성
    fun getQRCodeImage(email: String): String? {
        val qrCodeWriter = QRCodeWriter()
        var text = email
        val bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 200, 200)
        val pngOutputStream = ByteArrayOutputStream()
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream)
        //이거 유저 디비에 저장하기
        val fileName = "qrcode/"+UUID.randomUUID().toString()+".png"
        //multifile 변환
        val resizePhoto=CustomMultipartFile(fileName, "PNG", "image/png", pngOutputStream.toByteArray())
        val objectMetadata = ObjectMetadata()
        objectMetadata.contentLength = resizePhoto.size
        objectMetadata.contentType = resizePhoto.contentType
        try {
            amazonS3.putObject(
                PutObjectRequest(bucket, fileName, resizePhoto.inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead)
            )
        } catch (e: IOException) {
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 업로드에 실패했습니다.")
        }
        System.gc()
        return fileName
    }
    fun deleteImage(fileName: String?) {
        amazonS3.deleteObject(DeleteObjectRequest(bucket, fileName))
    }

    private fun createFileName(fileName: String): String? {
        return UUID.randomUUID().toString() + getFileExtension(fileName)
    }

    private fun getFileExtension(fileName: String): String {
        return try {
            fileName.substring(fileName.lastIndexOf("."))
        } catch (e: StringIndexOutOfBoundsException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 형식의 파일($fileName) 입니다.")
        }
    }

    fun uploadImage(photo:MultipartFile): String? {
        //파일이 없는 경우
        if (photo.isEmpty) {
            throw PhotoNotFoundException()
        }

        val fileName = "images/"+createFileName(photo.originalFilename!!);
        val fileFormat = photo.contentType!!.split("/")[1]
        val image = ImageIO.read(photo.inputStream)
        //resizer 실횅
        val resizePhoto = resizer(fileName, fileFormat, image, 400)
        val objectMetadata = ObjectMetadata()
        objectMetadata.contentLength = resizePhoto.size
        objectMetadata.contentType = resizePhoto.contentType
        try {
            amazonS3.putObject(
                PutObjectRequest(bucket, fileName, resizePhoto.inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead)
            )
//            resizePhoto.inputStream.close()
        } catch (e: IOException) {
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 업로드에 실패했습니다.")
        }
        System.gc()
        return fileName
    }

    fun uploadItemImage(url:String,itemIdx:Int): String? {
        //파일이 없는 경우
        if (url.isEmpty()) {
            throw PhotoNotFoundException()
        }
        var item = itemRepository.findById(itemIdx).orElseThrow(::ItemNotFoundException)

        val imgURL = URL(url)
        var itemName=item.barcode

        val fileName = "images/$itemName.png"
        val fileFormat = "png"
        val image = ImageIO.read(imgURL)
        //원본 먼저 저장하기
        val photo = resizer(fileName, fileFormat, image, 1000)
        val objectMetadata = ObjectMetadata()
        objectMetadata.contentLength = photo.size
        objectMetadata.contentType = photo.contentType
        try {
            amazonS3.putObject(
                PutObjectRequest(bucket, fileName, photo.inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead)
            )
//            resizePhoto.inputStream.close()
        } catch (e: IOException) {
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 업로드에 실패했습니다.")
        }


        //resizer 실횅
        val resizePhoto = resizer(fileName, fileFormat, image, 100)
//        val objectMetadata = ObjectMetadata()
        objectMetadata.contentLength = resizePhoto.size
        objectMetadata.contentType = resizePhoto.contentType
        try {
            amazonS3.putObject(
                PutObjectRequest(bucket, "images/$itemName"+"_thumb.png", resizePhoto.inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead)
            )
//            resizePhoto.inputStream.close()
        } catch (e: IOException) {
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 업로드에 실패했습니다.")
        }
        System.gc()
        return fileName
    }
    @Transactional
    fun resizer(fileName: String, fileFormat: String?, image: BufferedImage, width: Int): MultipartFile {
        return try {
            val originWidth = image.width
            val originHeight = image.height
            val imageMarvin = MarvinImage(image)
            val scale = Scale()
            scale.load()
            scale.setAttribute("newWidth", width)
            scale.setAttribute("newHeight", width * originHeight / originWidth) //비율유지를 위해 높이 유지
            scale.process(imageMarvin.clone(), imageMarvin, null, null, false)
            val imageNoAlpha: BufferedImage = imageMarvin.bufferedImageNoAlpha
            val baos = ByteArrayOutputStream()
            ImageIO.write(imageNoAlpha, fileFormat, baos)
            baos.flush()
            CustomMultipartFile(fileName, fileFormat, "image/png", baos.toByteArray())
        } catch (e: IOException) {
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "파일을 줄이는데 실패했습니다.")
        }
    }
    class CustomMultipartFile(
        private val fileName: String,
        private val originalFileName: String?,
        @JvmField val contentType: String,
        private val fileContent: ByteArray,
    ) : MultipartFile {
        private val destPath = System.getProperty("java.io.tmpdir")

        override fun getInputStream(): InputStream {
            return fileContent.inputStream()
        }

        override fun getName(): String {
            return fileName
        }

        override fun getOriginalFilename(): String? {
            return originalFileName
        }

        override fun getContentType(): String? {
            return contentType
        }

        override fun isEmpty(): Boolean {
            return fileContent.isEmpty()
        }

        override fun getSize(): Long {
            return fileContent.size.toLong()
        }

        override fun getBytes(): ByteArray {
            return fileContent
        }

        override fun transferTo(dest: File) {
            val fileOutputStream = FileOutputStream(dest);
            fileOutputStream.write(fileContent);
        }

    }
}