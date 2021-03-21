package org.zfz7.service

import javassist.NotFoundException
import org.springframework.stereotype.Service
import org.zfz7.domain.Relay
import org.zfz7.repository.PeerRepository
import org.zfz7.repository.RelayRepository
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.io.FileWriter




@Service
class RelayService(
  val relayRepository: RelayRepository,
  val peerRepository: PeerRepository
) {
  fun createRelay() {
    val privatKey = runBashCommand("wg genkey")
    val relay = Relay(
      privateKey = privatKey,
      publicKey = runBashCommand("echo $privatKey | wg pubkey")
    )
    relayRepository.save(relay)
  }

  fun writeWg0File() {
    val relay = relayRepository.findTopBy() ?: throw NotFoundException("")
    val peers = peerRepository.findAll()
    val writer = FileWriter("wg0.conf")

    writer.write("[Interface]\n")
    writer.write("Address = ${relay.address}\n")
    writer.write("ListenPort = ${relay.listenPort}\n")
    writer.write("PrivateKey = ${relay.privateKey}\n")
    for (peer in peers) {
      writer.write("[Peer]\n")
      writer.write("PublicKey = ${peer.publicKey}\n")
      writer.write("PresharedKey = ${peer.preSharedKey}\n")
      writer.write("AllowedIPs = ${peer.address}\n")
    }
    writer.close()
    runBashCommand("sudo cp wg0.conf /etc/wireguard/",false)
    runBashCommand("sudo systemctl enable wg-quick@wg0",false)
    runBashCommand("sudo systemctl restart wg-quick@wg0",false)
  }

  private fun runBashCommand(command: String, check: Boolean = true): String {
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
    if(check)
      assert(exitCode == 0)
    return line1
  }
}