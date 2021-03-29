package org.zfz7.service

import javassist.NotFoundException
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import org.zfz7.repository.PeerRepository
import org.zfz7.repository.RelayRepository
import java.io.BufferedReader
import java.io.File
import java.io.FileWriter
import java.io.InputStreamReader

interface WgService {
  fun getPrivateKey(): String
  fun getPublicKey(privateKey:String): String
  fun getPreSharedKey(): String
  fun writeRelayConfigFile()
}


@Service
@Profile("!test")
class WgServiceProd (
  val relayRepository: RelayRepository,
  val peerRepository: PeerRepository,
        ): WgService {
  private val logger = LoggerFactory.getLogger(StartUp::class.java)

  override fun getPrivateKey(): String {
    logger.info("private key created")
    return runBashCommand("wg genkey")
  }

  override fun getPublicKey(privateKey: String): String {
    logger.info("public key created")
    return runBashCommand("echo $privateKey | wg pubkey")
  }

  override fun getPreSharedKey(): String {
    logger.info("pre shared key created")
    return runBashCommand("wg genpsk")
  }
  override fun writeRelayConfigFile() {
    val relay = relayRepository.findTopBy() ?: throw NotFoundException("")
    val peers = peerRepository.findAll()

    val writer: FileWriter = try{
      FileWriter(File("./config","wg0.conf"))
    } catch(e: Exception){
      FileWriter("wg0.conf")
    }

    writer.write("[Interface]\n")
    writer.write("Address = ${relay.address}\n")
    writer.write("ListenPort = ${relay.listenPort}\n")
    writer.write("PrivateKey = ${relay.privateKey}\n")
    writer.write(("PostUp = iptables -A FORWARD -i %i -j ACCEPT; iptables -A FORWARD -o %i -j ACCEPT; iptables -t nat -A POSTROUTING -o eth0 -j MASQUERADE\n"))
    writer.write(("PostDown = iptables -D FORWARD -i %i -j ACCEPT; iptables -D FORWARD -o %i -j ACCEPT; iptables -t nat -D POSTROUTING -o eth0 -j MASQUERADE\n"))
      for (peer in peers) {
      writer.write("[Peer]\n")
      writer.write("PublicKey = ${peer.publicKey}\n")
      writer.write("PresharedKey = ${peer.preSharedKey}\n")
      writer.write("AllowedIPs = ${peer.address}\n")
    }
    writer.close()
    runBashCommand("sudo docker-compose -f ~/relay/docker-compose.prod.yml restart wireguard")
  }

  private fun runBashCommand(command: String): String {
    val isWindows = System.getProperty("os.name")
      .toLowerCase().startsWith("windows")
    val builder = ProcessBuilder()
    if (isWindows) {
      throw Exception("Windows not supported")
    } else {
      builder.command("sh", "-c", command)
    }
    builder.directory(File(System.getProperty("user.home")))
    val process = builder.start()
    val reader = BufferedReader(InputStreamReader(process.inputStream))
    val line1 = reader.readLine()?: "null"
    val exitCode = process.waitFor()
    assert(exitCode == 0)
    return line1
  }
}

@Service
@Profile("test")
class WgServiceTest : WgService {
  override fun getPrivateKey(): String {
    return "test-privateKey"
  }

  override fun getPublicKey(privateKey: String): String {
    return "test-publicKey"
  }

  override fun getPreSharedKey(): String {
    return "test-preSharedKey"
  }

  override fun writeRelayConfigFile() {
  }
}