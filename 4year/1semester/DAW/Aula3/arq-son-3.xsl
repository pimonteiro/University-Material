<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    
    version="2.0">
    
    <xsl:output method="xhtml" indent="yes" encoding="UTF-8"/>
    
    <xsl:template match="/">
        <html>
            <head>
                <title>Arquivo Sonoro de Ernesto Veiga de Oliveira</title>
                <meta charset="UTF-8"/>
                <link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css"/> 
            </head>
            <body>
                <xsl:for-each select="arq/doc">
                    <table class="w3-table">
                        <tr>
                            <th>Titulo</th><td><xsl:value-of select="./tit"/></td>
                        </tr>
                        <tr>
                            <th>Local</th><td><xsl:value-of select="./local"/></td>
                        </tr>
                        <tr>
                            <th>Província</th><td><xsl:value-of select="./prov"/></td>
                        </tr>
                        <tr>
                            <th>Músico</th><td><xsl:value-of select="./musico"/></td>
                        </tr>
                        <tr>
                            <th>Duração</th><td><xsl:value-of select="duracao"/> min</td>
                        </tr>
                    </table>
                    <hr/>
                </xsl:for-each>
            </body>
        </html>
    </xsl:template>
</xsl:stylesheet>