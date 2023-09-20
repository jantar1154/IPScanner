# Reason for this project
I wanted to find out every IP adddress on my network, but the network in question was configured so that users couldn't ping broadcast. This tool also finds out open port so you can find vulnerabilities in your LAN.

# How to use the IP Scanner
1. Download (ideally newest version of) IPScanner.jar from the Releases tab.
2. From your command line, cd into the directory with IPScanner.jar in it.
3. Run the following command: ```java -jar IPScanner.jar```
4. Input a speed value (lower is faster). This is because some networks are configured to disconnect the user if the user makes too many ICMP requests.
5. Choose whether you wish for the tool to also look for most used open ports.
6. The tool automatically finds out your IP address and subnet mask and pings every possible IP on your network.
